package com.example.flo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    val song = Song("", "", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf: SharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        setPlayerStatus(spf.getBoolean("isPlaying", false))

        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }

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
            setPlayerStatus(true)
        }
        binding.songBtnPauseIv.setOnClickListener {
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
        val spf : SharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean("isPlaying", isPlaying)
        editor.apply()

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
}