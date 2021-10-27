package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

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
        binding.homeWidget1AlbumIv.setOnClickListener {
            //context~: fragment를 어디서 변경하는지
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

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

    fun setPage() {
        if(currentPage == 4)
            currentPage = 0
        binding.homeBannerVp.setCurrentItem(currentPage, true)
        currentPage+=1
    }

    inner class PagerRunnable : Runnable {
        override fun run() {
            while(true) {
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }
}