package com.example.flo


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.db.Album
import com.example.flo.db.Like
import com.example.flo.db.Song
import com.example.flo.db.SongDatabase
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    val menu = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false // 앨범 좋아요

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //Home에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        //Home에서 넘어온 데이터를 뷰에 반영
        setViews(album)
        setClickListeners(album)

        //ROOM_DB
        val songs = getSongs(album.id) //앨범안에 있는 수록곡들을 불러옵니다.
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하시면 됩니다.

        //init viewpager
        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumMenuTl, binding.albumContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()

        if (isLiked) { //앨범 좋아요
            binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {

        binding.albumBtnBackIv.setOnClickListener {
            //context~: fragment를 어디서 변경하는지
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val userId = getJwt()

        binding.albumBtnLikeIv.setOnClickListener {
            if (isLiked) { //좋아요 -> 좋아요 취소
                binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            } else { //좋아요 취소 -> 좋아요
                binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }

//        binding.albumMenu1Cl.setOnClickListener {
//            selectMenu(1)
//        }
//        binding.albumMenu2Cl.setOnClickListener {
//            selectMenu(2)
//        }
//        binding.albumMenu3Cl.setOnClickListener {
//            selectMenu(3)
//        }
//
//        binding.albumMixSwitch1.setOnClickListener {
//            setMixStatus(true)
//        }
//        binding.albumMixSwitch2.setOnClickListener {
//            setMixStatus(false)
//        }
//
//        binding.albumBtnSelectAllCl.setOnClickListener {
//            setSelectAll()
//        }
//
//        binding.albumBtnPlayAllCl.setOnClickListener {
//            Toast.makeText(activity, "플레이리스트가 재생목록에 담겼습니다. 중복곡은 제외됩니다.", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun getJwt(): Int {
        //fragment에서 sharedPreferences 사용하기
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!! //fragment에서는 이렇게 씀
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean { //좋아요가 눌려있는지 안눌려있는지 확인하는 함수
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) { //좋아요 지우는 함수
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song> {
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.SongDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }

//    private fun selectMenu(menu: Int) {
//        when (menu) {
//            1 -> {
//                binding.albumMenu1Tv.setTextColor(Color.parseColor("#4735EC"))
//                binding.albumMenu2Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu3Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu1V.visibility = View.VISIBLE
//                binding.albumMenu2V.visibility = View.INVISIBLE
//                binding.albumMenu3V.visibility = View.INVISIBLE
//            }
//            2 -> {
//                binding.albumMenu1Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu2Tv.setTextColor(Color.parseColor("#4735EC"))
//                binding.albumMenu3Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu1V.visibility = View.INVISIBLE
//                binding.albumMenu2V.visibility = View.VISIBLE
//                binding.albumMenu3V.visibility = View.INVISIBLE
//            }
//            3 -> {
//                binding.albumMenu1Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu2Tv.setTextColor(Color.parseColor("#FF000000"))
//                binding.albumMenu3Tv.setTextColor(Color.parseColor("#4735EC"))
//                binding.albumMenu1V.visibility = View.INVISIBLE
//                binding.albumMenu2V.visibility = View.INVISIBLE
//                binding.albumMenu3V.visibility = View.VISIBLE
//            }
//        }
//    }
//
//    private fun setMixStatus(Mix: Boolean) {
//        if (Mix) {
//            binding.albumMixSwitch1.visibility = View.GONE
//            binding.albumMixSwitch2.visibility = View.VISIBLE
//            Toast.makeText(activity, "내 취향 순서로 변경했습니다.", Toast.LENGTH_SHORT).show()
////            Toast.showCustomToast
//        } else {
//            binding.albumMixSwitch1.visibility = View.VISIBLE
//            binding.albumMixSwitch2.visibility = View.GONE
//            Toast.makeText(activity, "일반 곡 순서로 변경했습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun setSelectAll() {
//        if (binding.albumCheckTv.text === "전체선택") {
//            binding.albumCheck1Iv.visibility = View.GONE
//            binding.albumCheck2Iv.visibility = View.VISIBLE
//            binding.albumCheckTv.text = "선택해제"
//            binding.albumCheckTv.setTextColor(Color.parseColor("#4735EC"))
//
//        } else {
//            binding.albumCheck1Iv.visibility = View.VISIBLE
//            binding.albumCheck2Iv.visibility = View.GONE
//            binding.albumCheckTv.text = "전체선택"
//            binding.albumCheckTv.setTextColor(Color.parseColor("#FF000000"))
//
//
//        }
//    }
}
