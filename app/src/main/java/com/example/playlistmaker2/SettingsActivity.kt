package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.btBack)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }
        val helpText = findViewById<TextView>(R.id.tvWriteToSupport)
        helpText.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:rinatgordey@yandex.ru")
            intent.putExtra(Intent.EXTRA_SUBJECT,"Сообщение разработчикам и разработчицам приложения Playlist Maker")
            intent.putExtra(Intent.EXTRA_TEXT,"Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(intent)
        }

        val agreement = findViewById<TextView>(R.id.tvAgreement)
        agreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(intent)
        }

        val switchDarkMode: Switch = findViewById(R.id.themeSwitch)
        switchDarkMode.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                setNightMode(true)
            } else {
                setNightMode(false)
            }
        }
    }
    private fun setNightMode(enabled: Boolean) {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> if (!enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreateActivity()
            }
            Configuration.UI_MODE_NIGHT_NO -> if (enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreateActivity()
            }
        }
    }
    private fun recreateActivity() {
        recreate()
    }
}