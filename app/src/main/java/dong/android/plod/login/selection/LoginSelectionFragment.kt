package dong.android.plod.login.selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import dong.android.plod.R
import dong.android.plod.databinding.FragmentLoginSelectionBinding
import dong.android.plod.util.autoCleared

class LoginSelectionFragment : Fragment() {

    private var binding by autoCleared<FragmentLoginSelectionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFunctions()
    }

    private fun setFunctions() {
        binding.tvSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.move_to_sign_up_email)
        }

        binding.tvSignIn.setOnClickListener {

        }

        binding.tvSignInNaver.setOnClickListener {

        }

        binding.tvSignInKakao.setOnClickListener {

        }
    }

}