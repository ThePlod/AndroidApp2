package dong.android.plod.login.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentSignInBinding
import dong.android.plod.di.App
import dong.android.plod.login.activity.LoginActivity
import dong.android.plod.main.activity.MainActivity

class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel: SignInViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFunctions()
        initSocketFunctions()
    }

    private fun initFunctions() {
        binding.btnSignIn.setOnClickListener {
            emitLoginInfoToServer()
        }
    }

    private fun initSocketFunctions() {
        App.socket.on("login success") {
            if (it[0] != null) {
                val loginSuccess =
                    Gson().fromJson(it[0].toString(), HashMap<String, Any>()::class.java)

                Log.d("loginSuccess", loginSuccess.toString())

                if (loginSuccess["success"] == true) {
                    binding.root.post {
                        Toast.makeText(requireContext(),
                            loginSuccess["msg"].toString(),
                            Toast.LENGTH_SHORT).show()

                        App.pref.setRefreshToken(loginSuccess["refreshToken"].toString())
                        App.pref.setAccessToken(loginSuccess["accessToken"].toString())

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.putExtra("userID", id)
                        startActivity(intent)
                        (activity as LoginActivity).finish()
                    }


                } else {
                    binding.root.post {
                        Toast.makeText(requireContext(),
                            loginSuccess["msg"].toString(),
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Log.d("null", it.toString())
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        App.socket.off("login success")
    }
}