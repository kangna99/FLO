package com.example.flo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecommendViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeIndicator1Fragment()
            1 -> HomeIndicator2Fragment()
            else -> HomeIndicator3Fragment()
        }
    }
}