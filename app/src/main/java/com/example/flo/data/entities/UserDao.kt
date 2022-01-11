package com.example.flo.data.entities

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User? //없을 수도 있으니 null 처리

    @Query("SELECT name FROM UserTable WHERE id = :id")
    fun getUserNickname(id: Int): String
}