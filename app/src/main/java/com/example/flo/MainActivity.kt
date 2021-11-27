package com.example.flo

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.db.Album
import com.example.flo.db.Song
import com.example.flo.db.SongDatabase
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var song: Song
    private lateinit var songDB: SongDatabase
    private lateinit var player: Player


    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null // ?을 붙여 nullable(null 값이 들어올 수 있음) 선언

//    //GSON
//    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("생명주기", "main onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initNavigation() //Fragment 변경, 하단메뉴
        inputDummyAlbums()
        inputDummySongs()

//        player = Player(song.playTime, song.isPlaying)
//        player.start()

        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "main onStart")

        val spf = getSharedPreferences(
            "song",
            MODE_PRIVATE
        ) //MODE_PRIVATE : 이 앱에서만 이 sharedPreferences에 접근 가능하다.

        val songId = spf.getInt("songId", 0) //spf를 이용해 DB에서 데이터를 받아옴
        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) { //가장 처음에만 실행
            songDB.SongDao().getSong(1)
        } else {
            songDB.SongDao().getSong(songId)
        }

        setMiniPlayer(song)
        Log.d("song ID", song.id.toString())

        player = Player(song.playTime, song.isPlaying, song.second)
        Log.d("상태", song.playTime.toString() + song.isPlaying.toString() + song.second.toString())
        player.start()

        if (song.isPlaying) {
            mediaPlayer?.start()
        }

        binding.mainPlayBtn.setOnClickListener {
            setPlayerStatus(true)
            song.isPlaying = true
            player.isPlaying = true
            mediaPlayer?.start() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
            Log.d("Log test", song.title + song.singer + song.playTime + song.isPlaying)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            song.isPlaying = false
            player.isPlaying = false
            mediaPlayer?.pause() //nullable로 선언한 변수는 함수 사용 시에도 ?를 붙여야 함.
            Log.d("Log test", song.title + song.singer + song.playTime + song.isPlaying)
        }

        binding.mainPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("progress", "onProgressChanged")
                song.second = seekBar!!.progress * song.playTime / 1000
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("progress", "onStartTrackingTouch")
                song.second = seekBar!!.progress * song.playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                song.second = seekBar!!.progress * song.playTime / 1000
                player.interrupt()
                player = Player(song.playTime,song.isPlaying,song.second)
                player.start()
                mediaPlayer?.seekTo(song.second * 1000)
                Log.d("progress", "onStopTrackingTouch")
            }
        })
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "main onPause")

        if (binding.mainPauseBtn.isVisible) {
            mediaPlayer?.pause() //미디어 플레이어 중지
            song.isPlaying = true
        } else {
            mediaPlayer?.pause() //미디어 플레이어 중지
            song.isPlaying = false
        }
        song.second = (song.playTime * binding.mainPlayerSb.progress) / 1000

        songDB.SongDao().update(song)

        player.interrupt()

//        song.second = (binding.mainPlayerSb.progress * song.playTime) / 1000
//        Log.d("생명주기", "초 : " + song.second)
//
//        //sharedPreferences : 액티비티가 pause 될 때 sharedPreferences에 song 객체 정보를 전부 전달
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용
//
//        //Gson : 객체 <-> Json의 역할을 함
//        val json = gson.toJson(song) //gson을 json으로 변환
//        editor.putString("song", json) //Json 객체를 통째로 sharedPreferences에 넘겨준 것
//
//        editor.apply()
        editor.putInt("songId", song.id) //Json 객체를 통째로 sharedPreferences에 넘겨준 것
        editor.apply()
        Log.d("isPlaying", song.isPlaying.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer
        mediaPlayer?.release()
    }


    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
//        binding.mainPlayerSb.progress = song.second * 1000 / song.playTime


        //mediaPlayer에 음악 연동
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)

        setPlayerStatus(song.isPlaying)

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

    //RoomDB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                "2021.03.25",
                "정규",
                "댄스 팝",
                R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter",
                "방탄소년단 (BTS)",
                "2021.05.21",
                "싱글",
                "댄스",
                R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Next Level",
                "aespa",
                "2021.05.17",
                "싱글",
                "댄스 팝",
                R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "신호등",
                "이무진",
                "2021.05.14",
                "싱글",
                "락",
                R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "정규 2집 '마음, 하나'",
                "폴킴",
                "2019.10.07",
                "미니",
                "알앤비, 발라드",
                R.drawable.img_album_exp5
            )
        )
    }
    private fun inputDummySongs() {
        Log.d("dummy", "들어옴")
        songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.SongDao().getSongs()

        if (songs.isNotEmpty()) return

        Log.d("dummy", "들어옴2")
        songDB.SongDao().insert(
            Song(
                "lilac_iu",
                "라일락",
                "아이유(IU)",
                215,
                false,
                0,
                R.drawable.img_album_exp2,
                false,
                1,
                1
            )
        )


        songDB.SongDao().insert(
            Song(
                "flu_iu",
                "Flu",
                "아이유(IU)",
                190,
                false,
                0,
                R.drawable.img_album_exp2,
                false,
                1,
                2,
                false
            )
        )

        songDB.SongDao().insert(
            Song(
                "butter_bts",
                "Butter",
                "방탄소년단(BTS)",
                222,
                false,
                0,
                R.drawable.img_album_exp,
                false,
                2
            )
        )

        songDB.SongDao().insert(
            Song(
                "nextlevel_aespa",
                "Next Level",
                "에스파(AESPA)",
                221,
                false,
                0,
                R.drawable.img_album_exp3,
                false,
                3
            )
        )

        songDB.SongDao().insert(
            Song(
                "trafficlight_lee",
                "신호등",
                "이무진",
                231,
                false,
                0,
                R.drawable.img_album_exp4,
                false,
                4
            )
        )

        songDB.SongDao().insert(
            Song(
                "newday_paulkim",
                "New Day",
                "폴킴",
                243,
                false,
                0,
                R.drawable.img_album_exp5,
                false,
                5
            )
        )

        //확인용
        val songs_ = songDB.SongDao().getSongs()
        Log.d("DB DATA", songs_.toString())
    }

    inner class Player(private val playTime: Int, var isPlaying: Boolean, private var second: Int) :
        Thread() {

        override fun run() { //run 코드가 끝나면 쓰레드도 종료
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        second++

                        runOnUiThread { //handler를 쓰는 방법도 있음
                            binding.mainPlayerSb.progress = second * 1000 / playTime
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
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

