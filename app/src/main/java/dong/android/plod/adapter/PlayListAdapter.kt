package dong.android.plod.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dong.android.plod.databinding.ItemPlayListBinding
import dong.android.plod.model.SongData

class PlayListAdapter : RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<SongData>() {
        override fun areItemsTheSame(oldItem: SongData, newItem: SongData): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: SongData, newItem: SongData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    var isDeletable = false
    var deleteList = mutableListOf<SongData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(ItemPlayListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class PlayListViewHolder(private val binding: ItemPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData() {
            val data = differ.currentList[bindingAdapterPosition]

            binding.apply {
                tvSong.text = data.song
                tvSinger.text = data.singer
                tvGenre.text = data.genre
            }

            if (isDeletable) {
                binding.cbDelete.visibility = View.VISIBLE
            } else {
                binding.cbDelete.visibility = View.GONE
                deleteList.clear()
                binding.cbDelete.isChecked = false
            }

            binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        deleteList.add(differ.currentList[bindingAdapterPosition])
                        Log.d("deleteList", deleteList.toString())
                    }
                    false -> {
                        deleteList.remove(differ.currentList[bindingAdapterPosition])
                        Log.d("deleteList", deleteList.toString())
                    }
                }
            }
        }
    }
}