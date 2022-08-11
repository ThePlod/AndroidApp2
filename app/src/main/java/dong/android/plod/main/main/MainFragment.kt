package dong.android.plod.main.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import dong.android.plod.R
import dong.android.plod.databinding.FragmentMainBinding
import dong.android.plod.util.autoCleared

class MainFragment : Fragment() {

    private var binding by autoCleared<FragmentMainBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPlaylist.setOnClickListener {
            it.findNavController().navigate(R.id.move_to_playlist)
        }
    }
}