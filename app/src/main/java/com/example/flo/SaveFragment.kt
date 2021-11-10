package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSaveBinding
import com.google.gson.Gson

class SaveFragment : Fragment() {

    lateinit var binding : FragmentSaveBinding
    private var albumDatas = ArrayList<Album>();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveBinding.inflate(inflater, container, false)

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파(AESPA)", R.drawable.img_album_exp3))
            add(Album("신호등", "이무진", R.drawable.img_album_exp4))
            add(Album("New Day", "폴킴", R.drawable.img_album_exp5))
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파(AESPA)", R.drawable.img_album_exp3))
            add(Album("신호등", "이무진", R.drawable.img_album_exp4))
            add(Album("New Day", "폴킴", R.drawable.img_album_exp5))
        }

        //더미데이터랑 Adapter 연결
        val songRVAdapter = SongRVAdapter(albumDatas)
        //리사이클러뷰에 어댑터를 연결
        binding.saveSongRv.adapter = songRVAdapter

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {

            override fun onItemClick(album: Album) {

            }

            override fun onRemoveAlbum(position: Int) {
                songRVAdapter.removeItem(position)
            }
        })
        //레이아웃 매니저 설정
        binding.saveSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}