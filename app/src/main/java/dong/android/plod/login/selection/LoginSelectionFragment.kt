package dong.android.plod.login.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import dong.android.plod.R
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentLoginSelectionBinding

class LoginSelectionFragment : BaseFragment<FragmentLoginSelectionBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginSelectionBinding {
        return FragmentLoginSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFunctions()
    }

    private fun initFunctions() {
        binding.tvSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.move_to_sign_up_email)
        }

        binding.tvSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.move_to_sign_in)
        }
    }
}