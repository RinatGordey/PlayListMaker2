package com.example.playlistmaker2.mediaLibrary.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackLibrary.setOnClickListener {
            finish()
        }

        val adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout,
            binding.viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}