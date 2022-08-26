package dong.android.plod.login.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.databinding.FragmentSignInBinding
import dong.android.plod.di.App
import dong.android.plod.login.activity.LoginActivity
import dong.android.plod.util.autoCleared

class SignInFragment : Fragment() {

    private var binding by autoCleared<FragmentSignInBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getLoginSuccess()

        binding.btnSignIn.setOnClickListener {
            emitLoginInfoToServer()
        }
    }

    private fun emitLoginInfoToServer() {
        val loginInfo = linkedMapOf<String, String>().apply {
            this["id"] = binding.etLoginEmail.text.toString()
            this["password"] = binding.etLoginPassword.text.toString()
        }

        val loginInfoAsJson = Gson().toJson(loginInfo)
        App.socket.emit("login", loginInfoAsJson)
    }

    private fun getLoginSuccess() {
        App.socket.on("login success") {
            if (it[0] != null) {
                val loginSuccess =
                    Gson().fromJson(it[0].toString(), HashMap<String, Any>()::class.java)

                if (loginSuccess["success"] == true) {
                    binding.root.post {
                        findNavController().navigate(R.id.move_to_main)
                        (activity as LoginActivity).finish()
                    }
                } else {
                    // 로그인 실패
                }

                Log.d("login success", loginSuccess.toString())
            }
        }
    }
}