package com.example.flo.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.data.entities.Song
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