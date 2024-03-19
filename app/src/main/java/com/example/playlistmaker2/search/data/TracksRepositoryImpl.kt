package com.example.playlistmaker2.search.data

import com.example.playlistmaker2.search.data.dto.TrackRequest
import com.example.playlistmaker2.search.data.dto.TrackResponse
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Internet")
            }

            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        (SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)),
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}