package com.example.playlistmaker2.search.data.network

import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.dto.Response
import com.example.playlistmaker2.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    companion object {
        const val URL_API = "https://itunes.apple.com"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ItunesService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()

            val body = resp.body() ?: Response()
            val  response = Response()
            var code = response.resultCode
            return (body.apply { code = resp.code() }) as Response
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}