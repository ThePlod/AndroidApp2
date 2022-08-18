package dong.android.plod.main.playlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dong.android.plod.model.SongData
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.net.URISyntaxException

class PlayListViewModel : ViewModel() {

    var list = MutableLiveData<List<SongData>>()

    private lateinit var mSocket: Socket
    private val onConnect = Emitter.Listener {
        mSocket.emit("user id", "admin")
    }

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://211.199.155.142:1021")
            mSocket.connect()

            Log.d("setSocket", mSocket.toString())

            //최초 서버 연결되었을 때 Socket.EVENT_CONNECT 를 받고 onConnect(리스너) 실행
            mSocket.on(Socket.EVENT_CONNECT, onConnect)

            mSocket.on("server init") {
                if (it[0] != null) {
                    Log.d("server init", it[0] as String)
                }
            }

            //Get Whole List
            mSocket.on("whole list") {
                val tempList = mutableListOf<SongData>()

                if (it[0] != null) {
                    val json = JSONArray(it[0].toString())
                    for (i in 0 until json.length()) {
                        val songData = Gson().fromJson(json.getString(i), SongData::class.java)
                        tempList.add(songData)
                    }
                }
                list.postValue(tempList)
            }

            //Get Returned List
            mSocket.on("returned list") {
                val tempList = mutableListOf<SongData>()

                if (it[0] != null) {
                    val json = JSONArray(it[0].toString())
                    for (i in 0 until json.length()) {
                        Log.d("returned list", "$i : ${json.getString(i)}")
                        val songData = Gson().fromJson(json.getString(i), SongData::class.java)
                        tempList.add(songData)
                    }
                }
                list.postValue(tempList)
            }
        } catch (e: URISyntaxException) {
            Log.d("Socket Error", e.toString())
        }
    }

    @Synchronized
    fun emitSongData(data: SongData) {
        val dataAsJson = Gson().toJson(data)
        mSocket.emit("newly added", dataAsJson)

        mSocket.emit("song data with token", "token, {SongData}")
    }

    @Synchronized
    fun deleteItems(list: List<SongData>) {
        val dataAsJson = Gson().toJson(list)
        mSocket.emit("deleting items", dataAsJson)
    }
}