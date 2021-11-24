package com.example.flo

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.db.Song
import com.example.flo.db.SongDatabase
import com.google.gson.Gson


class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var player: Player
    //    private val handler = Handler(Looper.getMainLooper())

    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null // ?을 붙여 nullable(null 값이 들어올 수 있음) 선언

    //Gson
    private var gson: Gson = Gson()

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("생명주기", "song onCreate")
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList() //플레이리스트 만들기(songs 리스트 만드는 함수)
        initSong() //songs 리스트 내 position을 알아내서 setPlayer, startPlayer
        //setPlayer(뷰 렌더링, mediaPlayer create), startPlayer(시간 가는 스레드)
        initClickListener()
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "song onStart")

//        val spf = getSharedPreferences("song", MODE_PRIVATE) //MODE_PRIVATE : 이 앱에서만 이 sharedPreferences에 접근 가능하다.
//        val nowPos = spf.getInt("songId", 0)
//        val jsonSong = spf.getString("song", null)
//
//        song = gson.fromJson(jsonSong, Song::class.java) //Json -> 객체로 포맷 변환

//        player = Player(songs[nowPos].playTime, songs[nowPos].isPlaying, songs[nowPos].second)
//        mediaPlayer?.seekTo(songs[nowPos].second * 1000)
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "song onPause")

        if (binding.songBtnPauseIv.isVisible) {
            mediaPlayer?.pause() //미디어 플레이어 중지
            songs[nowPos].isPlaying = true
        } else {
            mediaPlayer?.pause() //미디어 플레이어 중지
            songs[nowPos].isPlaying = false
        }

        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerSb.progress) / 1000


        setPlayerStatus(false) //일시정지 상태일 때의 이미지로 전환

        songDB.SongDao().update(songs[nowPos])

        //sharedPreferences : 액티비티가 pause 될 때 sharedPreferences에 song 객체 정보를 전부 전달
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val editor = spf.edit() //sharedPreferences 조작할 때 사용

        editor.putInt("songId", songs[nowPos].id) //Json 객체를 통째로 sharedPreferences에 넘겨준 것
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
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startPlayer()
        setPlayer(songs[nowPos])

        if (songs[nowPos].isPlaying) {
            mediaPlayer?.start()
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun startPlayer() { //플레이어에 재생시간, 재생유무를 알려주고 재생하도록 함
        player = Player(songs[nowPos].playTime, songs[nowPos].isPlaying, songs[nowPos].second)
        player.start()
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        //뷰에 렌더링
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        Log.d("몇 초", song.playTime.toString())
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songPlayerSb.progress = song.second * 1000 / song.playTime
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        //mediaPlayer에 음악 연동
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)
    }


    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }

    private fun initClickListener() {
        binding.songPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("progress", "onProgressChanged")
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
                binding.songStartTimeTv.text = String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("progress", "onStartTrackingTouch")
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
                if(songs[nowPos].isPlaying) {
                    player.interrupt()
                    startPlayer()
                }
                else {
                    startPlayer()
                }
                mediaPlayer?.seekTo(songs[nowPos].second * 1000)
                binding.songStartTimeTv.text = String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
                Log.d("progress", "onStopTrackingTouch")
            }
        })
        //SongActivity 종료
        binding.songBtnDownIv.setOnClickListener {
            finish()
        }

        //재생
        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
            songs[nowPos].isPlaying = true
            mediaPlayer?.start() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
        }
        //일시정지
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
        }
        //이전곡
        binding.songBtnPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        //다음곡
        binding.songBtnNextIv.setOnClickListener {
            moveSong(+1)
        }

        //좋아요
        binding.songBtnLikeIv.setOnClickListener {
            setLikeStatus(songs[nowPos].isLike)
        }

        //싫어요
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

    private fun setLikeStatus(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.SongDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike) {
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
            showToast("좋아요 한 곡에 담겼습니다.")
//            Toast.makeText(this, "좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
            showToast("좋아요 한 곡이 취소되었습니다.")
//            Toast.makeText(this, "좋아요 한 곡이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }

        //확인용
        val songs_ = songDB.SongDao().getSongs()
        Log.d("DB DATA", songs_.toString())

    }

    private fun setUnlikeStatus(Unlike: Boolean) {
        if (Unlike) {
            binding.songBtnUnlike1Iv.visibility = View.GONE
            binding.songBtnUnlike2Iv.visibility = View.VISIBLE
        } else {
            binding.songBtnUnlike1Iv.visibility = View.VISIBLE
            binding.songBtnUnlike2Iv.visibility = View.GONE
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        player.isPlaying = isPlaying

        if (isPlaying) {
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE

        } else {
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
        }
        //putPlayStatus(isPlaying)
    }

    private fun setRepeatStatus(repeatMode: Int) {
        if (repeatMode == 1) {
            binding.songBtnRepeat1Iv.visibility = View.GONE
            binding.songBtnRepeat2Iv.visibility = View.VISIBLE
            binding.songBtnRepeat3Iv.visibility = View.GONE
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        } else if (repeatMode == 2) {
            binding.songBtnRepeat1Iv.visibility = View.GONE
            binding.songBtnRepeat2Iv.visibility = View.GONE
            binding.songBtnRepeat3Iv.visibility = View.VISIBLE
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        } else if (repeatMode == 3) {
            binding.songBtnRepeat1Iv.visibility = View.VISIBLE
            binding.songBtnRepeat2Iv.visibility = View.GONE
            binding.songBtnRepeat3Iv.visibility = View.GONE
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRandomStatus(randomMode: Boolean) {
        if (randomMode) {
            binding.songBtnRandom1Iv.visibility = View.GONE
            binding.songBtnRandom2Iv.visibility = View.VISIBLE
            Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songBtnRandom1Iv.visibility = View.VISIBLE
            binding.songBtnRandom2Iv.visibility = View.GONE
            Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveSong(direct: Int) {

        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        player.interrupt()
        startPlayer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        setPlayer(songs[nowPos])
    }

    private fun getPlayStatus(): Boolean {
        return intent.getBooleanExtra("isPlaying", false)
    }

    private fun putPlayStatus(isPlaying: Boolean) {
        intent.putExtra("isPlaying", isPlaying)
    }


    inner class Player(private val playTime: Int, var isPlaying: Boolean, private var second: Int) :
        Thread() {
//        private var second = 0

        override fun run() { //run 코드가 끝나면 쓰레드도 종료
            try {
                while (true) {
                    if (second >= playTime) { //노래가 끝까지 재생
                        break
                    }

                    if (isPlaying) {

                        sleep(1000)
                        second++

                        runOnUiThread { //handler를 쓰는 방법도 있음
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.songStartTimeTv.text =
                                String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    /*object SampleToast {

        fun createToast(context: Context, message: String): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: LayoutCustomToastBinding =
                LayoutCustomToastBinding.inflate(inflater, (ViewGr)
            setContentView(binding.root)

            binding.tvSample.text = message

            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 16.toPx())
                duration = Toast.LENGTH_LONG
                view = binding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }*/

    private fun showToast(string: String) {
        val context: Context = this@SongActivity
        val message: CharSequence = string
        val duration = Toast.LENGTH_SHORT

        val layout: View = layoutInflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.toast_container)
        )

        val text = layout.findViewById<TextView>(R.id.toast_text)
        text.text = message
        val toast = Toast(context)
        toast.view = layout
        toast.duration = duration

        toast.show()

        if(Build.VERSION_CODES.R >= 30) {

        } else {

        }
    }
}