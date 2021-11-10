package com.example.flo

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var player : Player
    //    private val handler = Handler(Looper.getMainLooper())
    private var song : Song = Song()
    //미디어 플레이어
    private var mediaPlayer : MediaPlayer? = null // ?을 붙여 nullable(null 값이 들어올 수 있음) 선언
    //Gson
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("생명주기", "song onCreate")
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSong()
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "song onStart")


        setButton()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //MODE_PRIVATE : 이 앱에서만 이 sharedPreferences에 접근 가능하다.
        val jsonSong = sharedPreferences.getString("song", null)

        song = gson.fromJson(jsonSong, Song::class.java) //Json -> 객체로 포맷 변환

        player = Player(song.playTime, song.isPlaying, song.second)
        mediaPlayer?.seekTo(song.second*1000)
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "song onPause")
        mediaPlayer?.pause() //미디어 플레이어 중지
        player.isPlaying = false //스레드 중지
        song.isPlaying = false
        song.second = (binding.songPlayerSb.progress * song.playTime) / 1000
        setPlayerStatus(false) //일시정지 상태일 때의 이미지로 전환

        //sharedPreferences : 액티비티가 pause 될 때 sharedPreferences에 song 객체 정보를 전부 전달
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용

        //Gson : 객체 <-> Json의 역할을 함
        val json = gson.toJson(song) //gson을 json으로 변환
        editor.putString("song", json) //Json 객체를 통째로 sharedPreferences에 넘겨준 것

        editor.apply()
    }

    override fun onDestroy() { //필요없는 리소스 해제
        super.onDestroy()
        Log.d("생명주기", "song onDestroy")
        player.interrupt() //스레드 종료
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("music")) {
            //song 전역변수에 intent 값 가져온거 넣기
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.music = intent.getStringExtra("music")!!
            song.second = intent.getIntExtra("second", 0)

            val music = resources.getIdentifier(song.music,"raw", this.packageName)

            //뷰에 렌더링
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            binding.songPlayerSb.progress = song.second*1000/song.playTime
            setPlayerStatus(song.isPlaying)

            //mediaPlayer에 음악 연동
            mediaPlayer = MediaPlayer.create(this, music)
        }
    }

    private fun setButton() {

        //SongActivity 종료
        binding.songBtnDownIv.setOnClickListener {
            finish()
        }

        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
        }

        //좋아요
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

        //반복재생
        binding.songBtnRepeat1Iv.setOnClickListener {
            setRepeatStatus(1)
        }
        binding.songBtnRepeat2Iv.setOnClickListener {
            setRepeatStatus(2)
        }
        binding.songBtnRepeat3Iv.setOnClickListener {
            setRepeatStatus(3)
        }

        //랜덤재생
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

    inner class Player(private val playTime : Int, var isPlaying : Boolean, private var second : Int) : Thread() {
//        private var second = 0

        override fun run() { //run 코드가 끝나면 쓰레드도 종료
            try {
                while(true) {
                    if(second >= playTime) { //노래가 끝까지 재생
                        break
                    }

                    if(isPlaying) {
                        binding.songPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                                Log.d("progress", "onProgressChanged")
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                Log.d("progress", "onStartTrackingTouch")
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                song.second = seekBar!!.progress * song.playTime / 1000
                                binding.songPlayerSb.progress = second*1000/playTime
                                Log.d("progress", "onStopTrackingTouch")
                            }
                        })

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


}