package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.db.Song
import java.util.ArrayList

class AlbumViewpagerAdapter (fragment : Fragment, private val songs : ArrayList<Song>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SongFragment(songs) //수록곡
            1 -> DetailFragment() //상세정보
            else -> VideoFragment() //영상
        }

    }

}