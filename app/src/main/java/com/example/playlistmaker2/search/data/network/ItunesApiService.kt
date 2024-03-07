package com.example.playlistmaker2.search.data.network

import com.example.playlistmaker2.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("/search?entity=song")
    fun search(@Query("term") expression: String): Call<TrackResponse>
}