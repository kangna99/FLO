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
    private lateinit var player : MainActivity.Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()


//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 215, getPlayStatus())
        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 215, false)
        Log.d("Log test", song.title + song.singer + song.playTime + song.isPlaying)
        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }

        binding.mainPlayBtn.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            Log.d("Log test", song.title + song.singer + song.playTime + song.isPlaying)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
            Log.d("Log test", song.title + song.singer + song.playTime + song.isPlaying)
        }

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
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.mainPlayBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

        } else {
            binding.mainPlayBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
//        putPlayStatus(isPlaying)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    inner class Player(private val playTime : Int, var isPlaying : Boolean) : Thread() {
        private var second = 0

        override fun run() { //run 코드가 끝나면 쓰레드도 종료
            try {
                while(true) {
                    if(second >= playTime) {
                        break
                    }

                    if(isPlaying) {
                        sleep(1000)
                        second++

                        runOnUiThread { //handler를 쓰는 방법도 있음
                            binding.mainPlayerSb.progress = second*1000/playTime
                        }
                    }
                }
            }catch (e:InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    override fun onDestroy() {
        player.interrupt() //쓰레드 종료
        super.onDestroy()
    }

//    private fun getPlayStatus(): Boolean {
//        return intent.getBooleanExtra("isPlaying", false)
//    }
//
//    private fun putPlayStatus(isPlaying: Boolean) {
//        intent.putExtra("isPlaying", isPlaying)
//        Log.d("상태", "isPlaying" + isPlaying)
//    }

}

