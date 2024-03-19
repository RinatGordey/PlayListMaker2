package com.example.playlistmaker2.search.domain.api

import com.example.playlistmaker2.search.domain.model.Track

interface HistoryRepository {

    fun getTracks():Array<Track>
    fun addTrack(track: Track):List<Track>
    fun cleanHistory()

}