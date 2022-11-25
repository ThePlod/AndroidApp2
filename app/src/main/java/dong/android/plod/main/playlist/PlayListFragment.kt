package dong.android.plod.main.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dong.android.plod.adapter.PlayListAdapter
import dong.android.plod.base.BaseFragment
import dong.android.plod.databinding.FragmentPlayListBinding
import dong.android.plod.main.activity.MainActivity
import dong.android.plod.main.activity.MainViewModel
import dong.android.plod.model.SongData

class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {

    private val viewModel: PlayListViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var isDeletable: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlayListBinding {
        return FragmentPlayListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initFunctions()
    }

    private fun initRecyclerView() {
        binding.rvPlaylist.apply {
            adapter = PlayListAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }

        mainViewModel.list.observe(viewLifecycleOwner) {
            Log.d("observing", it.toString())
            (binding.rvPlaylist.adapter as PlayListAdapter).differ.submitList(it)
        }
    }

    private fun initFunctions() {
        binding.ivAdd.setOnClickListener {
            AddDialogFragment().show((activity as MainActivity).supportFragmentManager, "add")
        }

        binding.ivDelete.setOnClickListener {
            changeDeleteMode()
            if ((binding.rvPlaylist.adapter as PlayListAdapter).deleteList.isNotEmpty()) {
                deleteItems()
            }
        }

        binding.cbDeleteAll.setOnCheckedChangeListener { compoundButton, isChecked ->
            when (isChecked) {
                true -> {

                }
                else -> {

                }
            }
        }
    }

    private fun changeDeleteMode() {
        (binding.rvPlaylist.adapter as PlayListAdapter).isDeletable = !isDeletable
        isDeletable = !isDeletable
        (binding.rvPlaylist.adapter as PlayListAdapter).notifyDataSetChanged()

        if (!isDeletable) {
            binding.cbDeleteAll.visibility = View.GONE
            binding.cbDeleteAll.isChecked = false
        } else {
            binding.cbDeleteAll.visibility = View.VISIBLE
        }
    }

    private fun deleteItems() {
        viewModel.deleteItems((binding.rvPlaylist.adapter as PlayListAdapter).deleteList)
    }
}