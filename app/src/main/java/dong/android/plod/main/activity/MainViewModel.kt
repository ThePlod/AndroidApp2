package dong.android.plod.main.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dong.android.plod.di.App
import dong.android.plod.model.SongData
import org.json.JSONArray
import java.net.URISyntaxException

class MainViewModel : ViewModel() {

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
                    Log.d("return list", it[0].toString())
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
}