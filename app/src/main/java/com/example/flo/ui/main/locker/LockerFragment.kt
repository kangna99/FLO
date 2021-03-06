package com.example.flo.ui.main.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.data.entities.SongDatabase
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.signin.LoginActivity
import com.example.flo.utils.getUserIdx
import com.example.flo.utils.getUserName
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    lateinit var userDB: SongDatabase

    val menu = arrayListOf("저장한 곡", "iPod 음악", "저장앨범")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        userDB = SongDatabase.getInstance(requireContext())!!

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerMenuTl, binding.lockerContentVp) {
                tab,position ->
            tab.text = menu[position]
        }.attach()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        val userIdx = getUserIdx(requireContext())//jwt를 가져오는 함수

        if(userIdx == 0) { //로그인하지 않은 상태
            binding.lockerLoginTv.text = "로그인"

            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else { //로그인 된 상태
            binding.lockerNicknameTv.text = getUserName(requireContext())
            binding.lockerLoginTv.text = "로그아웃"

            binding.lockerLoginTv.setOnClickListener {
                //로그아웃
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout() { //로그아웃 함수
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("userIdx")
        editor.apply()
    }


}