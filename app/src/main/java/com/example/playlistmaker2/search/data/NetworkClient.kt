package com.example.playlistmaker2.search.data

import com.example.playlistmaker2.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
