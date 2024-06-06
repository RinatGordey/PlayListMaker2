package com.example.playlistmaker2.player.ui.activity

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.ActivityPlayerDisplayBinding
import com.example.playlistmaker2.player.ui.mapper.TrackMapper
import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.player.ui.view_model.PlayerDisplayViewModel
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.loadTrackPicture
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerDisplayActivity : AppCompatActivity() {

    companion object {
        const val TRACK = "TRACK"
        const val LAST_TRACK = "LAST_TRACK"
        const val CURRENT_POSITION = "CURRENT_POSITION"
        const val IS_PLAYING = "IS_PLAYING"
    }

    private lateinit var binding: ActivityPlayerDisplayBinding
    private lateinit var lastTrack: TrackInfo
    private lateinit var mediaPlayer: MediaPlayer

    private val viewModel: PlayerDisplayViewModel by viewModel {
        parametersOf(lastTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()

        if (savedInstanceState != null) {
            lastTrack = savedInstanceState.getParcelable(LAST_TRACK)!!
        } else {
            val trackExtra = intent.getSerializableExtra(TRACK)
            if (trackExtra is Track) {
                lastTrack = TrackMapper().map(trackExtra)
            }
        }

        arrowBack(binding)

        viewModel.create()
        binding.playButton.isEnabled = true

        setInfo(binding, lastTrack)

        viewModel.getPlayingLiveData().observe(this) { playbackState ->
            if (playbackState.playes) {
                binding.playButton.setImageResource(R.drawable.ic_bt_pause)
            } else {
                binding.playButton.setImageResource(R.drawable.ic_play_bt)
            }
            binding.time.text = playbackState.position
        }

        binding.playButton.setOnClickListener {
            viewModel.play()
        }

        binding.likeButton.setOnClickListener {
            viewModel.likeClick()
        }

        viewModel.getFavoriteLiveData().observe(this) { isFavorite ->
            if (isFavorite) {
                binding.likeButton.setImageResource(R.drawable.ic_liked)
                binding.likeButton.setColorFilter(ContextCompat.getColor(this, R.color.ic_liked))
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like_bt)
                binding.likeButton.setColorFilter(Color.WHITE)
            }
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
            if (lastTrack.collectionName?.isEmpty() == true) {
                albumValue.isVisible = false
            } else {
                albumValue.isVisible = true
                albumValue.text = lastTrack.collectionName
            }
            val artworkUrl = lastTrack.artworkUrl100
            trackPicture.loadTrackPicture(artworkUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_PLAYING, mediaPlayer.isPlaying())
        outState.putInt(CURRENT_POSITION, mediaPlayer.currentPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isPlaying = savedInstanceState.getBoolean(IS_PLAYING)
        val currentPosition = savedInstanceState.getInt(CURRENT_POSITION)
        if (isPlaying) {
            mediaPlayer.start();
            mediaPlayer.seekTo(currentPosition);
        }
    }
}