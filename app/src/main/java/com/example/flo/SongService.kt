package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import com.example.flo.db.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongService {
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView) {
        this.lookView = lookView
    }

    fun getSongs() {

        val songService = getRetrofit().create(SongRetrofitInterface::class.java)

        //호출 전 loading
        lookView.onGetSongsLoading()

        //api 호출 후 응답받으면 callback
        songService.getSongs().enqueue(object : Callback<SongResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                Log.d("SONGSERVICE/API_RESPONSE", response.toString())

                if(response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("SONGSERVICE/API_RESPONSE-FLO", resp.toString())

                    when (resp.code) {
                        1000 -> lookView.onGetSongsSuccess(resp.result!!.songs)
                        else -> lookView.onGetSongsFailure(resp.code, resp.message)
                    }
                }
            }

            //네트워크 실패
            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                Log.d("SONGSERVICE/API_ERROR", t.message.toString())
                lookView.onGetSongsFailure(400, t.message.toString())
            }
        })
    }
}