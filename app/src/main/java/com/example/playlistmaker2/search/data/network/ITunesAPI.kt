package com.example.playlistmaker2.search.data.network

import com.example.playlistmaker2.search.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String): Call<TrackResponse>
}