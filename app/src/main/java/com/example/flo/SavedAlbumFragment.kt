package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedAlbumBinding
import com.example.flo.db.SongDatabase
import com.google.gson.Gson

class SavedAlbumFragment : Fragment() {

    lateinit var binding: FragmentSavedAlbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)
        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        //레이아웃 매니저 설정
        binding.lockerSaveAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //리스너 객체 생성 및 전달
        val lockerAlbumRVAdapter = AlbumLockerRVAdapter()

        lockerAlbumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(albumId: Int) {
                albumDB.albumDao().disLikeAlbum(getUserIdx(requireContext()), albumId)
                albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext()))
            }
        })

        //리사이클러뷰에 어댑터를 연결
        binding.lockerSaveAlbumRv.adapter = lockerAlbumRVAdapter

        lockerAlbumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext())) as ArrayList)
    }
}