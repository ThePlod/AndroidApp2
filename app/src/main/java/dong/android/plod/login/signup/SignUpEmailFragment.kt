package dong.android.plod.login.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.databinding.FragmentSignUpEmailBinding
import dong.android.plod.di.App
import dong.android.plod.util.autoCleared


class SignUpEmailFragment : Fragment() {

    private var binding by autoCleared<FragmentSignUpEmailBinding>()
    private val viewModel: SignUpEmailViewModel by viewModels()

    private var verificationCode = 0
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_email, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnGetVerificationCode.setOnClickListener {
            emitEmailToServer()
            getVerificationCode()
        }

        binding.btnVerificationCompleted.setOnClickListener {
            val action = SignUpEmailFragmentDirections.moveToSignUpPassword(email)
            it.findNavController().navigate(action)
        }
    }

    private fun emitEmailToServer() {
        App.socket.emit("sign up email", binding.etEmail.text.toString())
    }

    private fun getVerificationCode() {
        App.socket.on("verification code") {
            if (it[0] != null) {
                val verificationInfo =
                    Gson().fromJson(it[0].toString(), HashMap<String, Any>()::class.java)

                if (verificationInfo["success"] == true) {
                    showVerificationInput()
                    email = binding.etEmail.text.toString()
                    verificationCode = verificationInfo["number"].toString().toInt()
                } else {
                    showWrongEmailInputMsg(msg = verificationInfo["msg"].toString())
                }

                Log.d("verification code", verificationInfo.toString())
            }
        }
    }

    private fun showVerificationInput() {
        binding.root.post {
            binding.apply {
                tvWrongEmail.visibility = View.GONE

                tvVerificationCode.visibility = View.VISIBLE
                etVerificationCode.visibility = View.VISIBLE
                tvVerificationCodeSent.visibility = View.VISIBLE

                btnGetVerificationCode.visibility = View.GONE
                btnVerificationCompleted.visibility = View.VISIBLE
            }

            viewModel.verificationCodeInput.observe(viewLifecycleOwner) {
                binding.btnVerificationCompleted.isEnabled = it.toInt() == verificationCode
            }
        }
    }

    private fun showWrongEmailInputMsg(msg: String) {
        binding.root.post {
            binding.tvWrongEmail.apply {
                visibility = View.VISIBLE
                text = msg
            }
        }
    }
}