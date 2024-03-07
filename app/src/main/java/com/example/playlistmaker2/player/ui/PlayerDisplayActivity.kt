package com.example.playlistmaker2.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker2.R
import com.example.playlistmaker2.creator.Creator
import com.example.playlistmaker2.databinding.ActivityPlayerDisplayBinding
import com.example.playlistmaker2.glide.loadTrackPicture
import com.example.playlistmaker2.player.domain.models.State
import com.example.playlistmaker2.player.presentation.mapper.TrackMapper
import com.example.playlistmaker2.player.presentation.model.TrackInfo
import com.example.playlistmaker2.search.domain.models.Track
import com.google.gson.Gson

class PlayerDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerDisplayBinding
    companion object{
        private const val REFRESH_MILLIS = 500L
        private const val START_TIMER = "00:00"
    }

    private val playerInteractor = Creator.providePlayerInteractor()

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack(binding)

        val trackMapper = TrackMapper()
        val lastTrack = trackMapper.map(Gson()
            .fromJson(intent?.getStringExtra("LAST_TRACK"), Track::class.java))

        handler = Handler(Looper.getMainLooper())

        setInfo(binding, lastTrack)

        val url: String = lastTrack.previewUrl
        playerInteractor.createPlayer(url)
        binding.playButton.isEnabled = true
        binding.playButton.setImageResource(R.drawable.ic_play_bt)
        binding.time.text = START_TIMER

        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun arrowBack(binding: ActivityPlayerDisplayBinding) {
        binding.btBackArrow.setOnClickListener {
            finish()
        }
    }

    private fun setInfo(binding: ActivityPlayerDisplayBinding, lastTrack: TrackInfo) {
        with(binding) {
            nameOfTrack.text = lastTrack.trackName
            authorOfTrack.text = lastTrack.artistName
            durationValue.text = lastTrack.trackTime
            yearValue.text = lastTrack.releaseDate
            genreValue.text = lastTrack.primaryGenreName
            countryValue.text = lastTrack.country
            if (lastTrack.collectionName.isNullOrEmpty()) {
                album.isVisible = false
            } else {
                album.isVisible = true
                album.text = lastTrack.collectionName
            }
            val artworkUrl = lastTrack.artworkUrl100
            trackPicture.loadTrackPicture(artworkUrl)
        }
    }

    private fun playbackControl() {
        when (playerInteractor.getState()) {
            State.PLAYING -> {
                playerInteractor.pause()
                binding.playButton.setImageResource(R.drawable.ic_play_bt)
            }

            State.END -> {
                binding.time.text = START_TIMER
                statePlaying()
            }

            State.PREPARED, State.PAUSED, State.DEFAULT -> {
                statePlaying()
            }
        }
    }

    private fun statePlaying() {
        playerInteractor.play()
        binding.playButton.setImageResource(R.drawable.ic_bt_pause)
        handler?.post(createUpdateTimerTask())
    }
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val state: State = playerInteractor.getState()
                if (state == State.PLAYING) {
                    binding.time.text = playerInteractor.getCurrentPosition()
                    handler?.postDelayed(this, REFRESH_MILLIS)
                } else {
                    handler?.removeCallbacks(this)
                    if (state == State.END) {
                        binding.playButton.setImageResource(R.drawable.ic_play_bt)
                        binding.time.text = START_TIMER
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
        binding.playButton.setImageResource(R.drawable.ic_play_bt)
    }
    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
    }
}
