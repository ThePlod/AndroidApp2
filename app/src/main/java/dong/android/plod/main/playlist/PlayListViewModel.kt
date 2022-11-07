package dong.android.plod.main.playlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dong.android.plod.di.App
import dong.android.plod.model.SongData
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import java.net.URISyntaxException

class PlayListViewModel : ViewModel() {

    var list = MutableLiveData<List<SongData>>()

    @Synchronized
    fun setSocket() {
        try {
            //Get Whole List
            App.socket.on("whole list") {
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
            App.socket.on("returned list") {
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
        App.socket.emit("newly added", dataAsJson)
        App.socket.emit("song data with token", "token, {SongData}")
    }

    @Synchronized
    fun deleteItems(list: List<SongData>) {
        val dataAsJson = Gson().toJson(list)
        App.socket.emit("deleting items", dataAsJson)
    }
}