package com.example.flo.ui.main.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemLockerSongBinding

class SongLockerRVAdapter() : RecyclerView.Adapter<SongLockerRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    //클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(song : Song)
        fun onRemoveSong(songId : Int)
    }

    //리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListner: MyItemClickListener) {
        mItemClickListener = itemClickListner
    }

    //뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져준다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongLockerRVAdapter.ViewHolder {
        val binding: ItemLockerSongBinding = ItemLockerSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: SongLockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(songs[position]) }
        holder.binding.itemLockerSongMoreIv.setOnClickListener{
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    //데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemLockerSongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemLockerSongTitleTv.text = song.title
            binding.itemLockerSongSingerTv.text = song.singer
            binding.itemLockerSongCoverImgIv.setImageResource(song.coverImg!!)
        }
    }

}