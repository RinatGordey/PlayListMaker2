package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private val PREFS_NAME = "Prefs"

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val nightModeEnabled = sharedPreferences.getBoolean("nightMode", false)

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
        switchDarkMode.isChecked = nightModeEnabled
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setNightMode(isChecked)
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
        editor.putBoolean("nightMode", enabled)
        editor.apply()

        recreate() // Пересоздаем активность, чтобы изменения вступили в силу
    }
}