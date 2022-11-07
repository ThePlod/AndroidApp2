package dong.android.plod.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.di.App
import dong.android.plod.login.activity.LoginActivity
import dong.android.plod.main.activity.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            emitLoginInfo()
        }, 1000)

        initAutoLoginFunction()
    }

    private fun initAutoLoginFunction() {
        App.socket.on("access success") {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                if (it[0] != null) {
                    startMainActivity(it[0] as String)
                }
            } else {
                Log.d("err", "Activity Is Not Running !")
            }
        }

        App.socket.on("access expired refresh unexpired") {
            if (it[0] != null) {
                App.pref.setAccessToken(it[0] as String)
                emitLoginInfo()
            }
        }

        App.socket.on("access expired refresh expired") {
            startLoginActivity()
        }
    }

    private fun emitLoginInfo() {
        val loginInfo = linkedMapOf<String, String>().apply {
            this["accessToken"] = App.pref.getAccessToken()
            this["refreshToken"] = App.pref.getRefreshToken()
        }

        val loginInfoAsJson = Gson().toJson(loginInfo)

        App.socket.emit("verify", loginInfoAsJson)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainActivity(id: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userID", id)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        App.socket.off("access success")
        App.socket.off("access expired refresh unexpired")
        App.socket.off("access expired refresh expired")
    }
}