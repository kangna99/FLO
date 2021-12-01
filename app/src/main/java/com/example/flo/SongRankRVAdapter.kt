package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemSongRankBinding
import com.example.flo.db.Song

class SongRankRVAdapter(val context: Context) : RecyclerView.Adapter<SongRankRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    //뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져준다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongRankRVAdapter.ViewHolder {
        val binding: ItemSongRankBinding = ItemSongRankBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: SongRankRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
//        holder.binding..setOnClickListener {
//            removeSong(position)
//            mItemClickListener.onRemoveSong(songs[position].id)
//        }
    }

    //데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemSongRankBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongRankRankTv.text = song.songIdx.toString()
            binding.itemSongRankTitleTv.text = song.title
            binding.itemSongRankSingerTv.text = song.singer

            if(song.coverImgUrl == "") {
                Glide.with(context).load(song.coverImg!!).into(binding.itemSongRankCoverImgIv)
            } else {
                Glide.with(context).load(song.coverImgUrl).into(binding.itemSongRankCoverImgIv)
            }

        }
    }

}