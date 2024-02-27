package com.example.playlistmaker2.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker2.Track
import com.example.playlistmaker2.creator.Creator
import com.example.playlistmaker2.databinding.ActivityPlayerDisplayBinding
import com.example.playlistmaker2.glide.GlideCreator
import com.example.playlistmaker2.player.presentation.TrackPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerDisplayBinding
    private lateinit var presenter: TrackPresenter

    private val glide = GlideCreator()
    private var handler: Handler? = null

    private var arrowBack: ImageButton? = null
    private var trackPicture: ImageView? = null
    private var nameOfTrack: TextView? = null
    private var authorOfTrack: TextView? = null
    private var timeOfTrack : TextView? = null
    private var album : TextView? = null
    private var year : TextView? = null
    private var genre : TextView? = null
    private var country : TextView? = null
    private var play: FloatingActionButton? = null
    private var time: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack()

        setView()

        val lastTrack: Track = Gson()
            .fromJson(intent?.getStringExtra("LAST_TRACK"), Track::class.java)

        handler = Handler(Looper.getMainLooper())

        setInfo(lastTrack)

        play?.setOnClickListener {
            presenter.onPlayClicked(play!!, time!!)
        }

        val url: String = lastTrack.previewUrl
        presenter = Creator.providePresenter(url)
        play?.let { presenter.preparePlayer(it) }
    }
    private fun arrowBack() {
        arrowBack = binding.btBackArrow
        arrowBack?.setOnClickListener {
            finish()
        }
    }
    private fun setView() {
        trackPicture = binding.trackPicture
        nameOfTrack = binding.nameOfTrack
        authorOfTrack = binding.authorOfTrack
        timeOfTrack = binding.durationValue
        album = binding.albumValue
        year = binding.yearValue
        genre = binding.genreValue
        country = binding.countryValue
        play = binding.playButton
        time = binding.time
        arrowBack = binding.btBackArrow
    }
    private fun setInfo(lastTrack: Track) {
        nameOfTrack?.text = lastTrack.trackName
        authorOfTrack?.text = lastTrack.artistName
        val time = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(lastTrack.trackTime.toLong())
        timeOfTrack?.text = time
        album?.isVisible = true
        album?.text = lastTrack.collectionName
        if (lastTrack.releaseDate.length >= 4) {
            year?.text = lastTrack.releaseDate.substring(0, 4)
        } else {
            year?.isVisible = false
        }
        genre?.text = lastTrack.primaryGenreName
        country?.text = lastTrack.country
        trackPicture?.let { glide.setTrackPicture(it, lastTrack) }
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.delete()
    }
}
