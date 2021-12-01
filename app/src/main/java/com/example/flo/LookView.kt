package com.example.flo

import com.example.flo.db.Song

interface LookView {
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs: ArrayList<Song>)
    fun onGetSongsFailure(code: Int, message: String)
}