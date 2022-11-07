package dong.android.plod.main.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.gson.Gson
import dong.android.plod.R
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentMainBinding
import dong.android.plod.di.App

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPlaylist.setOnClickListener {
            it.findNavController().navigate(R.id.move_to_playlist)
        }
    }
}