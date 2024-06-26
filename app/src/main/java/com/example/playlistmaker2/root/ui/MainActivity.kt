package com.example.playlistmaker2.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.ActivityMainBinding
import com.example.playlistmaker2.db.AppDatabase

class MainActivity: AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, state, _ ->
            when (state.id) {
                R.id.playerDisplayFragment, R.id.createPlaylistFragment -> {
                    bottomNavigationView.isVisible = false
                }
                else -> {
                    bottomNavigationView.isVisible = true
                }
            }
        }
    }
}