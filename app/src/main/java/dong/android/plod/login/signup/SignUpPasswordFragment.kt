package dong.android.plod.login.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.databinding.FragmentSignUpPasswordBinding
import dong.android.plod.di.App
import dong.android.plod.util.autoCleared

class SignUpPasswordFragment : Fragment() {

    private var binding by autoCleared<FragmentSignUpPasswordBinding>()
    private val viewModel: SignUpPasswordViewModel by viewModels()
    private val args: SignUpPasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_password, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        passwordMatchFunction()

        binding.btnStart.setOnClickListener {
            emitSignUpInfoToServer()
            getSignUpSuccessFromServer()
        }
    }

    private fun emitSignUpInfoToServer() {
        val signUpInfo = linkedMapOf<String, String>().apply {
            this["id"] = args.email
            this["password"] = binding.etPassword.text.toString()
        }

        val signUpInfoAsJson = Gson().toJson(signUpInfo)
        App.socket.emit("sign up info", signUpInfoAsJson)
    }

    private fun getSignUpSuccessFromServer() {
        App.socket.on("sign up success") {
            if (it[0] != null) {
                val signUpSuccessInfo =
                    Gson().fromJson(it[0].toString(), HashMap<String, Any>()::class.java)

                if (signUpSuccessInfo["success"] == true) {
                    binding.root.post {
                        findNavController().navigate(R.id.move_to_login_selection)
                    }
                } else {
                    // 로그인 실패
                }

                Log.d("sign up success", it[0].toString())
            }
        }
    }

    private fun passwordMatchFunction() {
        viewModel.passWordRe.observe(viewLifecycleOwner) {
            if (it == viewModel.passWord.value) {
                binding.btnStart.isEnabled = true
                binding.tvWrongPassword.visibility = View.GONE
            } else {
                binding.btnStart.isEnabled = false
                binding.tvWrongPassword.visibility = View.VISIBLE
            }
        }
    }
}