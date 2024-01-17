package com.example.playlistmaker2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.databinding.ActivityPlayerDisplayBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerDisplayBinding

    private lateinit var arrowBack : ImageButton
    private lateinit var trackPicture: ImageView
    private lateinit var nameOfTrack: TextView
    private lateinit var authorOfTrack: TextView
    private lateinit var timeOfTrack : TextView
    private lateinit var album : TextView
    private lateinit var year : TextView
    private lateinit var genre : TextView
    private lateinit var country : TextView

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

        val lastTrack: Track? = Gson()
            .fromJson(intent?.getStringExtra("LAST_TRACK"), Track::class.java)

        nameOfTrack.text = lastTrack?.trackName
        authorOfTrack.text = lastTrack?.artistName
        val time = SimpleDateFormat("mm:ss", Locale
            .getDefault())
            .format(lastTrack?.trackTime?.toLong())
        timeOfTrack.text = time
        album.isVisible = lastTrack?.collectionName !=null
        album.text = lastTrack?.collectionName
        year.text = lastTrack?.releaseDate?.substring(0,4)
        genre.text = lastTrack?.primaryGenreName
        country.text = lastTrack?.country

        Glide.with(trackPicture)
            .load(lastTrack?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.ic_stub)
            .transform(RoundedCorners(30))
            .into(trackPicture)
    }
}