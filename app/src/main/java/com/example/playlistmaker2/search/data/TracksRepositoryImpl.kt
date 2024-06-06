package com.example.playlistmaker2.search.data

import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.db.data.converters.TrackDbConvertor
import com.example.playlistmaker2.search.data.dto.TrackDto
import com.example.playlistmaker2.search.data.dto.TrackRequest
import com.example.playlistmaker2.search.data.dto.TrackResponse
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.ErrorType
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    ) : TracksSearchRepository {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ErrorType.INTERNET.message))
            }

            200 -> {
                with(response as TrackResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            dateFormat.format(it.trackTimeMillis),
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR.message))
            }
        }
    }
}