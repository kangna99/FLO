package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.db.Song
import com.example.flo.db.SongDatabase

class SongFragment() : Fragment() {

    lateinit var binding : FragmentSongBinding
    lateinit var songDB: SongDatabase
    private var albumIdx : Int = 0

    init {

    }
    constructor(albumIdx: Int): this() {
        this.albumIdx = albumIdx
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
        setViews()
    }

    private fun initRecyclerview() {
        //레이아웃 매니저 설정
        binding.albumContentTrackRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //더미데이터랑 Adapter 연결
        val songRVAdapter = SongRVAdapter()

        //리사이클러뷰에 어댑터를 연결
        binding.albumContentTrackRv.adapter = songRVAdapter


        songRVAdapter.addSongs(songDB.SongDao().getSongsInAlbum(albumIdx) as ArrayList)
    }

    private fun setViews() {


    }
}