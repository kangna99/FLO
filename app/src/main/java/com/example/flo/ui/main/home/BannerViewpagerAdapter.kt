package com.example.flo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerViewpagerAdapter (fragment : Fragment) :FragmentStateAdapter(fragment) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

//    override fun getItemCount(): Int {
//        return fragmentList.size
//    }
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment : Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size - 1)
    }

}