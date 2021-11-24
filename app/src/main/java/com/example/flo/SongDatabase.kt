package com.example.flo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun SongDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null
        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,SongDatabase::class.java, "user-database") //다른 데이터베이스랑 이름 겹치면 안됨
                        .allowMainThreadQueries().build() //메인스레드에 일을 맡김, 더 효율적으로 구현하려면 워크스레드에 넘겨야함
                }
            }
            return instance
        }
    }
}