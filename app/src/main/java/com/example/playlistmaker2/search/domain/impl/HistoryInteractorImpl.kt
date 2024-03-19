package com.example.playlistmaker2.search.domain.impl

import com.example.playlistmaker2.search.domain.api.HistoryInteractor
import com.example.playlistmaker2.search.domain.api.HistoryRepository
import com.example.playlistmaker2.search.domain.model.Track

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository
):HistoryInteractor {
    override fun getTracks(): Array<Track> {
        return historyRepository.getTracks()
    }

    override fun addTrack(track: Track): List<Track> {
        return historyRepository.addTrack(track)
    }

    override fun cleanHistory() {
        historyRepository.cleanHistory()
    }
}
