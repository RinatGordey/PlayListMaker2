package com.example.playlistmaker2.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker2.databinding.ActivitySettingsBinding
import com.example.playlistmaker2.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSwitcherLiveData().observe(this) { darkThemeEnabled ->
            binding.icSwitch.isChecked = darkThemeEnabled }

        binding.btBackSet.setOnClickListener {
            finish()
        }

        binding.icSupport.setOnClickListener {
                viewModel.openSupport()
            }

            binding.icShare.setOnClickListener {
                viewModel.shareApp()
            }


            binding.icAgreement.setOnClickListener {
                viewModel.openTerms()
            }

            binding.icSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.switchTheme(checked)
            }
    }
}