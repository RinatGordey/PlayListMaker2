package com.example.playlistmaker2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        val buttonSetting = findViewById<Button>(R.id.button_settings)

        val buttonClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val buttonSearchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(buttonSearchIntent)
            }
        }
        buttonSearch.setOnClickListener(buttonClickListener)

        buttonMediaLibrary.setOnClickListener {
            val buttonMediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(buttonMediaLibraryIntent)
        }
        buttonSetting.setOnClickListener {
            val buttonSettingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(buttonSettingIntent)
        }
    }
}