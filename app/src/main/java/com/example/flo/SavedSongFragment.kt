package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedSongBinding
import com.example.flo.db.Song
import com.example.flo.db.SongDatabase
import com.google.gson.Gson

class SavedSongFragment : Fragment() {

    lateinit var binding: FragmentSavedSongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        //레이아웃 매니저 설정
        binding.saveSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //더미데이터랑 Adapter 연결
        val songRVAdapter = SongRVAdapter()

        //리사이클러뷰에 어댑터를 연결
        binding.saveSongRv.adapter = songRVAdapter

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {
            override fun onItemClick(song: Song) {

            }

            override fun onRemoveSong(songId: Int) {
                songDB.SongDao().updateIsLikeById(false, songId)
            }
        })

        songRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList)
    }
}