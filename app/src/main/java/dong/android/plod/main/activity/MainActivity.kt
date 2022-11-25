package dong.android.plod.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.di.App

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.setSocket()
        getDataFromDB()
    }

    private fun getDataFromDB() {
        val dbDataRequest = linkedMapOf<String, String>().apply {
            this["accessToken"] = App.pref.getAccessToken()
            this["dbType"] = "song db"
        }

        val dbDataRequestAsJson = Gson().toJson(dbDataRequest)
        App.socket.emit("db type", dbDataRequestAsJson)
    }
}