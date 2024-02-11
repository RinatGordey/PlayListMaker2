package com.example.playlistmaker2

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.databinding.ActivityPlayerDisplayBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerDisplayBinding

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_MILLIS = 500L
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null

    private lateinit var arrowBack: ImageButton
    private lateinit var trackPicture: ImageView
    private lateinit var nameOfTrack: TextView
    private lateinit var authorOfTrack: TextView
    private lateinit var timeOfTrack: TextView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var play: FloatingActionButton
    private lateinit var time: TextView
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowBack = binding.btBackArrow
        arrowBack.setOnClickListener {
            finish()
        }

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
        mediaPlayer = MediaPlayer()
        handler  = Handler(Looper.getMainLooper())

        val lastTrack: Track = Gson()
            .fromJson(intent?.getStringExtra("LAST_TRACK"), Track::class.java)

        nameOfTrack.text = lastTrack.trackName
        authorOfTrack.text = lastTrack.artistName
        val time = SimpleDateFormat(
            "mm:ss", Locale
                .getDefault()
        )
            .format(lastTrack.trackTime.toLong())
        timeOfTrack.text = time
        album.isVisible = lastTrack.collectionName != null
        album.text = lastTrack.collectionName
        if (lastTrack.releaseDate.length >= 4) {
            year.text = lastTrack.releaseDate.substring(0, 4)
        } else {
            year.isVisible = false
        }
        genre.text = lastTrack.primaryGenreName
        country.text = lastTrack.country

        Glide.with(trackPicture)
            .load(lastTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_stub)
            .transform(RoundedCorners(ConversionDpToPx.dpToPx(8F, trackPicture.context)))
            .into(trackPicture)

        val url = lastTrack.previewUrl

        mediaPlayer?.setDataSource(url)
        preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }
    }

    private fun startTimer() {
        handler?.post(
            createUpdateTimerTask()
        )
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.time.text = dateFormat.format(mediaPlayer?.currentPosition)
                    handler?.postDelayed(this, REFRESH_MILLIS)
                }
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer?.setOnCompletionListener {
            play.setImageResource(R.drawable.ic_play_bt)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        play.setImageResource(R.drawable.ic_bt_pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        play.setImageResource(R.drawable.ic_play_bt)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}