package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter (fragment : Fragment, val albumIdx : Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SongFragment(albumIdx) //수록곡
            1 -> DetailFragment() //상세정보
            else -> VideoFragment() //영상
        }

    }

}