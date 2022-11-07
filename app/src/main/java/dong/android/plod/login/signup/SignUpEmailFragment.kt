package dong.android.plod.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.gson.Gson
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentSignUpEmailBinding
import dong.android.plod.di.App

class SignUpEmailFragment : BaseFragment<FragmentSignUpEmailBinding>() {

    private val viewModel: SignUpEmailViewModel by viewModels()

    private var verificationCode = 0
    private var email = ""

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignUpEmailBinding {
        return FragmentSignUpEmailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFunctions()
    }

    private fun initFunctions() {
        binding.apply {
            btnGetVerificationCode.setOnClickListener {
                emitEmailToServer()
                getVerificationCode()
            }

            btnVerificationCompleted.setOnClickListener {
                if (etEmail.text.toString() == email) {
                    val action = SignUpEmailFragmentDirections.moveToSignUpPassword(email)
                    it.findNavController().navigate(action)
                }
            }

            etVerificationCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.updateVerificationText(p0.toString())
                }
            })

            viewModel.verificationCodeInput.observe(viewLifecycleOwner) {
                binding.btnVerificationCompleted.isEnabled = it.toInt() == verificationCode
            }
        }
    }

    private fun emitEmailToServer() {
        email = binding.etEmail.text.toString()
        App.socket.emit("sign up email", email)
    }

    private fun getVerificationCode() {
        App.socket.on("verification code") {
            if (it[0] != null) {
                val verificationInfo =
                    Gson().fromJson(it[0].toString(), HashMap<String, Any>()::class.java)

                if (verificationInfo["success"] == true) {
                    showVerificationInput()
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

    override fun onDestroyView() {
        super.onDestroyView()
        App.socket.off("sign up email")
        App.socket.off("verification code")
    }
}