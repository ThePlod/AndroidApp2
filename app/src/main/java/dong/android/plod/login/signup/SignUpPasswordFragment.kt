package dong.android.plod.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentSignUpPasswordBinding
import dong.android.plod.di.App

class SignUpPasswordFragment : BaseFragment<FragmentSignUpPasswordBinding>() {

    private val viewModel: SignUpPasswordViewModel by viewModels()
    private val args: SignUpPasswordFragmentArgs by navArgs()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignUpPasswordBinding {
        return FragmentSignUpPasswordBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFunctions()
        initPasswordMatchFunction()
    }

    private fun initFunctions() {
        binding.apply {
            btnStart.isEnabled = false
            btnStart.setOnClickListener {
                emitSignUpInfoToServer()
                getSignUpSuccessFromServer()
            }

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.updatePasswordText(p0.toString())
                }
            })

            etPasswordRe.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.updatePasswordReText(p0.toString())
                }
            })
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

    private fun initPasswordMatchFunction() {
        binding.apply {
            viewModel.passWordRe.observe(viewLifecycleOwner) {
                if (it == viewModel.passWord.value) {
                    if (viewModel.passWord.value != "") {
                        btnStart.isEnabled = true
                        tvWrongPassword.visibility = View.GONE
                    }
                } else {
                    btnStart.isEnabled = false
                    tvWrongPassword.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        App.socket.off("sign up success")
    }
}