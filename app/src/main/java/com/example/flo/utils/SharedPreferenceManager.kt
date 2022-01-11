package com.example.flo.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveJwt(context: Context, jwt: String) {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("jwt", jwt)
    editor.apply()
}

fun getJwt(context: Context): String {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf.getString("jwt", "")!!
}

fun saveUserIdx(context: Context, userIdx: Int) {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("userIdx", userIdx)
    editor.apply()
}

fun getUserIdx(context: Context): Int {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("userIdx", 0)
}

fun saveUserName(context: Context, name: String) {
    val spf = context.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("name", name)
    editor.apply()
}

fun getUserName(context: Context): String? {
    val spf = context.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)

    return spf.getString("name", "")
}
