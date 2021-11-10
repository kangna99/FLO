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
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>();

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
//        binding.homeWidget1AlbumIv.setOnClickListener {
//            //context~: fragment를 어디서 변경하는지
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파(AESPA)", R.drawable.img_album_exp3))
            add(Album("신호등", "이무진", R.drawable.img_album_exp4))
            add(Album("New Day", "폴킴", R.drawable.img_album_exp5))
        }

        //더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        //리사이클러뷰에 어댑터를 연결
        binding.homeTodayRv.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })
        //레이아웃 매니저 설정
        binding.homeTodayRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


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

    private fun changeAlbumFragment(album: Album) {
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