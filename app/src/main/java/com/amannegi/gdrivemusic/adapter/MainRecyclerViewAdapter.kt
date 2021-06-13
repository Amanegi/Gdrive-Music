package com.amannegi.gdrivemusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amannegi.gdrivemusic.databinding.SongRowLayoutBinding
import com.google.api.services.drive.model.File

class MainRecyclerViewAdapter(
    private val files: MutableList<File>,
    private val itemCLickListener: ItemClickListener
) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.SongViewHolder>() {

    inner class SongViewHolder(private val binding: SongRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(file: File) {
            var songName = file.name
            // remove .mp3 from end
            songName = songName.removeSuffix(".mp3")
            binding.songName.text = songName
            // set random tint to song image
//            val rnd = Random
//            val randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//            binding.songImage.background.setColorFilter(randomColor, PorterDuff.Mode.SRC_ATOP)
            binding.songImage.text = songName[0].toString()
        }

        override fun onClick(v: View) {
            itemCLickListener.onClick(v, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding =
            SongRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int = files.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
}