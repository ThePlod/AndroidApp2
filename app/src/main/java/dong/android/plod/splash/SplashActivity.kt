package dong.android.plod.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import dong.android.plod.R
import dong.android.plod.login.activity.LoginActivity
import dong.android.plod.main.activity.MainActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class SplashActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setSocket()
        tempMoveToLogin()

    }

    private fun tempMoveToLogin() {
        findViewById<TextView>(R.id.splash_text).setOnClickListener {
            startLoginActivity()
        }
    }

    private fun setSocket() {
        mSocket = IO.socket("http://211.199.155.142:1021")
        mSocket.connect()

        val onConnect = Emitter.Listener {
            mSocket.emit("user id", "admin")
            // "admin" 에 App.pref.getUserName()이 들어갈 것
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)

        mSocket.on("user connected as") {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (it[0] != null) {
                        val id = it[0] as String
                        Log.d("user connected as", id)

                        if (id == "") {
                            startLoginActivity()
                        } else {
                            startMainActivity(id)
                        }
                    }
                }, 1000)
            } else {
                Log.d("user connected as", "Activity Is Not Running !")
            }
        }
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
}