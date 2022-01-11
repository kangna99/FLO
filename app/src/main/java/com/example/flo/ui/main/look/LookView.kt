package com.example.flo.ui.main.look

import com.example.flo.data.entities.Song

interface LookView {
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs: ArrayList<Song>)
    fun onGetSongsFailure(code: Int, message: String)
}