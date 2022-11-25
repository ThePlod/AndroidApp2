package dong.android.plod.main.playlist

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dong.android.plod.di.App
import dong.android.plod.model.SongData

class PlayListViewModel : ViewModel() {

    @Synchronized
    fun emitSongData(data: SongData) {
        val map = hashMapOf(
            "accessToken" to App.pref.getAccessToken(),
            "songData" to data
        )
        val dataAsJson = Gson().toJson(map)
        App.socket.emit("add song data", dataAsJson)
    }

    @Synchronized
    fun deleteItems(list: List<SongData>) {
        val map = hashMapOf(
            "accessToken" to App.pref.getAccessToken(),
            "newList" to list
        )

        val dataAsJson = Gson().toJson(map)
        App.socket.emit("delete song data", dataAsJson)
    }
}