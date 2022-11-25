package dong.android.plod.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketInitiator {

    private lateinit var mSocket: Socket
    private val onConnect = Emitter.Listener {
        //자동로그인
        mSocket.emit("user id", "admin")
    }

    @Synchronized
    fun setSocket(): Socket? {
        try {
            //http://211.199.155.142:1021
            mSocket = IO.socket("http://192.168.1.116:3000")
            mSocket.connect()

            //최초 서버 연결되었을 때 Socket.EVENT_CONNECT 를 받고 onConnect(리스너) 실행
            mSocket.on(Socket.EVENT_CONNECT, onConnect)

            mSocket.on("server init") {
                if (it[0] != null) {
                    Log.d("server init", it[0] as String)
                }
            }

            return mSocket
        } catch (e: URISyntaxException) {
            Log.d("Socket Error", e.toString())
            return null
        }
    }
}