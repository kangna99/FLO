package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.db.Album
import com.example.flo.db.SongDatabase
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albums = ArrayList<Album>();

    private lateinit var songDB: SongDatabase

    var currentPage = 0
    private val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }
    val thread = Thread(PagerRunnable())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        //레이아웃 매니저 설정
        binding.homeTodayRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albums)

        //리스너 객체 생성 및 전달
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {

            override fun onItemClick(album: Album) {
                startAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        //리사이클러뷰에 어댑터를 연결
        binding.homeTodayRv.adapter = albumRVAdapter


        //배너(banner)
        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        //상단 추천음악
        val recommendAdapter = RecommendViewpagerAdapter(this)
        binding.homeRecommendVp.adapter = recommendAdapter
        binding.homeRecommendInd.setViewPager(binding.homeRecommendVp)

        thread.start()

        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        //쓰레드 시작
//        thread.start()
//    }
//
//    override fun onPause() {
//        //쓰레드 종료
//        thread.interrupt()
//        super.onPause()
//    }

    private fun startAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    fun setPage() {
        if (currentPage == 4)
            currentPage = 0
        binding.homeBannerVp.setCurrentItem(currentPage, true)
        currentPage += 1
    }

    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }
}