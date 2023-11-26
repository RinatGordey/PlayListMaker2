package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val PREFS_NAME = "Prefs"
        const val NIGHT_MODE = "nightMode"
        const val TEXT_PLAIN = "text/plain"
    }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val nightModeEnabled = sharedPreferences.getBoolean(NIGHT_MODE, false)

        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.btBackSet) // кнопка назад в MainActivity
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            backIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(backIntent)
        }

        val helpButton = findViewById<ImageView>(R.id.icSupport) // кнопка в техподдержку
        helpButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.apply {
                type = TEXT_PLAIN
                data = Uri.parse(getString(R.string.mail_from_me))
                putExtra(Intent.EXTRA_SUBJECT,getString(R.string.mail_title))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.mail_text))
            }
            startActivity(intent)
        }

        val agreementButton = findViewById<ImageView>(R.id.icAgreement) // кнопка соглашение
        agreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.push_agreement)))
            startActivity(intent)
        }

        val switchDarkMode: Switch = findViewById(R.id.icSwitch) // кнопка переключалка темы
        switchDarkMode.isChecked = nightModeEnabled
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setNightMode(isChecked)
        }
        val shareButton = findViewById<ImageView>(R.id.icShare) // кнопка делиться приложением
        shareButton.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, R.string.link_to_share)
                type = TEXT_PLAIN
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)))
            }
        }
    }
    private fun setNightMode(enabled: Boolean) {
        val darkMode = if (enabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(darkMode)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(NIGHT_MODE, enabled)
        editor.apply()

        recreate() // Пересоздаем активность, чтобы изменения вступили в силу
    }
}