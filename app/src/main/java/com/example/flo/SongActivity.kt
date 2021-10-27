package com.example.flo

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    private val song : Song = Song()
    private lateinit var player : Player
//    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.songBtnDownIv.setOnClickListener {
            finish()
        }
        binding.songBtnLike1Iv.setOnClickListener {
            setLikeStatus(true)
        }
        binding.songBtnLike2Iv.setOnClickListener {
            setLikeStatus(false)
        }
        binding.songBtnUnlike1Iv.setOnClickListener {
            setUnlikeStatus(true)
        }
        binding.songBtnUnlike2Iv.setOnClickListener {
            setUnlikeStatus(false)
        }

        binding.songBtnPlayIv.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
        }
        binding.songBtnPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
        }

        binding.songBtnRepeat1Iv.setOnClickListener {
            setRepeatStatus(1)
        }
        binding.songBtnRepeat2Iv.setOnClickListener {
            setRepeatStatus(2)
        }
        binding.songBtnRepeat3Iv.setOnClickListener {
            setRepeatStatus(3)
        }
        binding.songBtnRandom1Iv.setOnClickListener {
            setRandomStatus(true)
        }
        binding.songBtnRandom2Iv.setOnClickListener {
            setRandomStatus(false)
        }
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime")) {
            //song 전역변수에 intent 값 가져온거 넣기
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)

            //뷰에 렌더링
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            setPlayerStatus(song.isPlaying)
        }
    }

    private fun setLikeStatus(Like : Boolean) {
        if(Like) {
            binding.songBtnLike1Iv.visibility = View.GONE
            binding.songBtnLike2Iv.visibility = View.VISIBLE
            Toast.makeText(this,"좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
        }else {
            binding.songBtnLike1Iv.visibility = View.VISIBLE
            binding.songBtnLike2Iv.visibility = View.GONE
            Toast.makeText(this,"좋아요 한 곡이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUnlikeStatus(Unlike : Boolean) {
        if(Unlike) {
            binding.songBtnUnlike1Iv.visibility = View.GONE
            binding.songBtnUnlike2Iv.visibility = View.VISIBLE
        }else {
            binding.songBtnUnlike1Iv.visibility = View.VISIBLE
            binding.songBtnUnlike2Iv.visibility = View.GONE
        }
    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE

        }else {
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
        }
        //putPlayStatus(isPlaying)
    }

    private fun setRepeatStatus(repeatMode : Int) {
        if(repeatMode == 1) {
            binding.songBtnRepeat1Iv.visibility = View.GONE
            binding.songBtnRepeat2Iv.visibility = View.VISIBLE
            binding.songBtnRepeat3Iv.visibility = View.GONE
            Toast.makeText(this,"전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }else if(repeatMode == 2){
            binding.songBtnRepeat1Iv.visibility = View.GONE
            binding.songBtnRepeat2Iv.visibility = View.GONE
            binding.songBtnRepeat3Iv.visibility = View.VISIBLE
            Toast.makeText(this,"현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }else if(repeatMode == 3){
            binding.songBtnRepeat1Iv.visibility = View.VISIBLE
            binding.songBtnRepeat2Iv.visibility = View.GONE
            binding.songBtnRepeat3Iv.visibility = View.GONE
            Toast.makeText(this,"반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRandomStatus(randomMode : Boolean) {
        if(randomMode) {
            binding.songBtnRandom1Iv.visibility = View.GONE
            binding.songBtnRandom2Iv.visibility = View.VISIBLE
            Toast.makeText(this,"재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        }else {
            binding.songBtnRandom1Iv.visibility = View.VISIBLE
            binding.songBtnRandom2Iv.visibility = View.GONE
            Toast.makeText(this,"재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPlayStatus() : Boolean {
        return intent.getBooleanExtra("isPlaying", false)
    }

    private fun putPlayStatus(isPlaying : Boolean) {
        intent.putExtra("isPlaying", isPlaying)
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
                            binding.songPlayerSb.progress = second*1000/playTime
                            binding.songStartTimeTv.text = String.format("%02d:%02d", second/60, second%60)
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
}