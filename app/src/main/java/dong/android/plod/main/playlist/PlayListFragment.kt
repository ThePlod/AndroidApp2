package dong.android.plod.main.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dong.android.plod.databinding.FragmentPlayListBinding
import dong.android.plod.util.autoCleared

class PlayListFragment : Fragment() {

    private var binding by autoCleared<FragmentPlayListBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

}