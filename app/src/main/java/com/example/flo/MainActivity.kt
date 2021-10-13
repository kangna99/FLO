package com.example.flo

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val song = Song("", "", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf: SharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        setPlayerStatus(spf.getBoolean("isPlaying", false))

        //val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), getPlayStatus())
        song.title = binding.mainMiniplayerTitleTv.text.toString()
        song.singer = binding.mainMiniplayerSingerTv.text.toString()

        //Log.d("Log test", song.title + song.singer)

        binding.mainPlayerLayout.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }
        
        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

    }

    override fun onRestart() {
        super.onRestart()

        val spf: SharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        setPlayerStatus(spf.getBoolean("isPlaying", false))
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        val spf: SharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean("isPlaying", isPlaying)
        editor.apply()

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }

        //putPlayStatus(isPlaying)
    }

    private fun getPlayStatus(): Boolean {
        return intent.getBooleanExtra("isPlaying", false)
    }

    private fun putPlayStatus(isPlaying: Boolean) {
        intent.putExtra("isPlaying", isPlaying)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

}

