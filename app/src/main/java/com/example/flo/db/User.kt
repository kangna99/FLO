package com.example.flo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    var nickname: String,
    var email: String,
    var password: String,

) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
