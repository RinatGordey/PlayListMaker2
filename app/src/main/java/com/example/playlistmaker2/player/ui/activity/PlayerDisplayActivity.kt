package com.example.playlistmaker2.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var binding: ActivityPlayerDisplayBinding
    private lateinit var lastTrack: TrackInfo

    val viewModel: PlayerDisplayViewModel by viewModel{
        parametersOf(lastTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack(binding)

        lastTrack = TrackMapper().map(
            intent.getSerializableExtra("track") as Track)

        viewModel.create()
        binding.playButton.isEnabled = true

        setInfo(binding, lastTrack)

        viewModel.getPlayingLiveData().observe(this) { playbackState ->
            if(playbackState.playes) {
                binding.playButton.setImageResource(R.drawable.ic_bt_pause)
            } else {
                binding.playButton.setImageResource(R.drawable.ic_play_bt)
            }
            binding.time.text = playbackState.position
        }

        binding.playButton.setOnClickListener {
            viewModel.play()
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
}
