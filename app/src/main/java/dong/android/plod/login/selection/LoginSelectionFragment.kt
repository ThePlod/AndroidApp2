package dong.android.plod.login.selection

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import dong.android.plod.R
import dong.android.plod.databinding.FragmentLoginSelectionBinding

class LoginSelectionFragment : Fragment() {

    private var _binding: FragmentLoginSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginSelectionBinding.inflate(layoutInflater)
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
            it.findNavController().navigate(R.id.move_to_sign_in)
        }

        binding.tvSignInNaver.setOnClickListener {

        }

        binding.tvSignInKakao.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}