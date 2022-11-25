package dong.android.plod.di

import android.app.Application
import android.widget.Toast
import dong.android.plod.pref.SharedPreferencesHelper
import dong.android.plod.socket.SocketInitiator
import io.socket.client.Socket
import kotlin.system.exitProcess

class App : Application() {

    companion object {
        lateinit var pref: SharedPreferencesHelper
        lateinit var socketInitiator: SocketInitiator
        lateinit var socket: Socket
    }

    override fun onCreate() {
        super.onCreate()
        socketInitiator = SocketInitiator()

        try {
            socket = socketInitiator.setSocket()!!
        } catch (e: NullPointerException) {
            Toast.makeText(applicationContext, "서버가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            exitProcess(0)
        }

        initPref()
    }

    private fun initPref() {
        pref = SharedPreferencesHelper(applicationContext)

        if (pref.getAccessToken() == "") {
            pref.setAccessToken("no access token")
        }

        if (pref.getRefreshToken() == "") {
            pref.setRefreshToken("no refresh token")
        }
    }
}