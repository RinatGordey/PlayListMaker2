package com.example.playlistmaker2
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    companion object{
        const val SEARCH_ACTIVITY = "SearchActivity"
        const val SEARCH_TEXT = "searchText"
    }

    private lateinit var searchEditText: EditText
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private var searchText: String = ""
    private val listTracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageButton>(R.id.btBackSearch)
        backButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(SEARCH_TEXT, searchText)
            setResult(Activity.RESULT_OK, returnIntent)
            finish() // Закрываем эту активити при нажатии "назад"
        }
        trackAdapter = TrackAdapter()

        recyclerViewTrack()

        listTracks.addAll(arrayTrackList)

        searchEditText = findViewById(R.id.edSearch)

        val clearButton = findViewById<ImageButton>(R.id.btClear)

        // Восстановить значение из SharedPreferences, если оно было сохранено
        val sharedPreferences = getSharedPreferences(SEARCH_ACTIVITY, Context.MODE_PRIVATE)
        searchText = sharedPreferences.getString(SEARCH_TEXT, "") ?: ""
        searchEditText.setText(searchText)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT, "")
            searchEditText.setText(searchText)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                searchText = s.toString()

            }
            override fun afterTextChanged(s: Editable) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()

            // Скрыть клавиатуру
            val hideKeyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            // Очистить значение из SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString(SEARCH_TEXT, "")
            editor.apply()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchEditText.setText(searchText)
    }

    override fun onPause() {
        super.onPause()

        // Сохранить значение введенного текста в SharedPreferences
        val sharedPreferences = getSharedPreferences(SEARCH_ACTIVITY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SEARCH_TEXT, searchText)
        editor.apply()
    }
    private fun recyclerViewTrack() {
        trackRecyclerView = findViewById(R.id.rvTrack)
        val layoutManager = LinearLayoutManager(this)
        trackRecyclerView.layoutManager = layoutManager

        trackAdapter.setTrackList(arrayTrackList)

        trackRecyclerView.adapter = trackAdapter

    }
}