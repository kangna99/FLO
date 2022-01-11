package com.example.flo.data.entities

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") //테이블의 모든 song 리스트로 가져오기
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id") //id에 해당하는 특정 song 객체로 가져오기
    fun getSong(id:Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike") //id에 해당하는 특정 song 객체로 가져오기
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int): List<Song>
}