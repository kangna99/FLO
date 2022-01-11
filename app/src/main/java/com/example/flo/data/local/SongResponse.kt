package com.example.flo.data.local

import com.example.flo.data.entities.Song
import com.google.gson.annotations.SerializedName

data class SongResult(@SerializedName("songs") val songs: ArrayList<Song>)

data class SongResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SongResult?
    )
