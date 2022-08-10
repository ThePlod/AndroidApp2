package dong.android.plod.main.playlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dong.android.plod.adapter.PlayListAdapter
import dong.android.plod.databinding.FragmentPlayListBinding
import dong.android.plod.main.activity.MainActivity
import dong.android.plod.model.SongData
import dong.android.plod.util.autoCleared

class PlayListFragment : Fragment() {

    private var binding by autoCleared<FragmentPlayListBinding>()
    private val viewModel: PlayListViewModel by activityViewModels()

    private var isDeletable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSocket()
        setRecyclerView()
        setFunction()
    }

    private fun setRecyclerView() {
        binding.rvPlaylist.apply {
            adapter = PlayListAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.list.observe(viewLifecycleOwner) {
            Log.d("observing", it.toString())
            (binding.rvPlaylist.adapter as PlayListAdapter).differ.submitList(it)
        }
    }

    private fun setFunction() {
        binding.ivAdd.setOnClickListener {
            AddDialogFragment().show((activity as MainActivity).supportFragmentManager, "add")
        }

        binding.ivDelete.setOnClickListener {
            if (!isDeletable) {
                changeDeleteMode()
            } else {
                if ((binding.rvPlaylist.adapter as PlayListAdapter).deleteList.isEmpty()) {
                    changeDeleteMode()
                } else {
                    deleteItems()
                    changeDeleteMode()
                }
            }
        }

        binding.cbDeleteAll.setOnCheckedChangeListener { compoundButton, isChecked ->
            when (isChecked) {
                true -> {

                }
                else -> {}
            }
        }

        binding.tvTitle.setOnClickListener {
            (binding.rvPlaylist.adapter as PlayListAdapter).differ.submitList(
                listOf(SongData("song", "singer", "genre", ""),
                    SongData("song", "singer", "genre", ""),
                    SongData("song", "singer", "genre", ""),
                    SongData("song", "singer", "genre", ""))
            )
        }
    }

    private fun changeDeleteMode() {
        (binding.rvPlaylist.adapter as PlayListAdapter).isDeletable = !isDeletable
        isDeletable = !isDeletable
        (binding.rvPlaylist.adapter as PlayListAdapter).notifyDataSetChanged()

        if (!isDeletable) {
            binding.cbDeleteAll.visibility = View.GONE
        } else {
            binding.cbDeleteAll.visibility = View.VISIBLE
        }
    }

    private fun deleteItems() {
        viewModel.deleteItems((binding.rvPlaylist.adapter as PlayListAdapter).deleteList)
    }

}