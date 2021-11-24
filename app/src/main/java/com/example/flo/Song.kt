package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    var music : String = "", //현재 재생 음악파일
    var title : String = "", //제목
    var singer : String = "", //가수
    var playTime : Int = 0, //총 재생 시간
    var isPlaying : Boolean = false, //현재 재생 유무
    var second : Int = 0, //현재 재생 시간
    var coverImg: Int? = null, //커버이미지
    var isLike: Boolean = false //좋아요
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
