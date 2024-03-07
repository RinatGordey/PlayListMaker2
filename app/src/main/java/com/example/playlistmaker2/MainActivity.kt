package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker2.search.ui.SearchActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.btSearch)
        val buttonMediaLibrary = findViewById<Button>(R.id.btMediaLibrary)
        val buttonSetting = findViewById<Button>(R.id.btSettings)

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