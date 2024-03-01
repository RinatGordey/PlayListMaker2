package com.example.playlistmaker2.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.SearchHistory
import com.example.playlistmaker2.search.domain.models.Track
import com.example.playlistmaker2.TrackAdapter
import com.example.playlistmaker2.TrackResponse
import com.example.playlistmaker2.databinding.ActivitySearchBinding
import com.example.playlistmaker2.player.ui.PlayerDisplayActivity
import com.example.playlistmaker2.search.data.network.ItunesService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val SEARCH_ACTIVITY = "SearchActivity"
        const val SEARCH_TEXT = "searchText"
        const val URL_API = "https://itunes.apple.com"
        const val HISTORY_KEY = "key_for_history"
        const val PREFS = "prefs"
        const val NUMBER_TEN = 10
        const val NUMBER_NINE = 9
        const val NUMBER_ZERO = 0
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchRunnable = Runnable { startSearch(binding.edSearch.text.toString()) }
    private val clickRunnable = Runnable { isClickAllowed = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ItunesService::class.java)

    private val trackList = ArrayList<Track>()

    private val recentTracks = ArrayList<Track>()
    private val recentAdapter = TrackAdapter(recentTracks)
    private val adapter = TrackAdapter(trackList)
    private val searchHistory by lazy { SearchHistory() }
    private lateinit var edSearch: EditText
    private lateinit var placeholderView: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var btClear: ImageButton
    private lateinit var rvTrack: RecyclerView
    private lateinit var btBackSearch: ImageView
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvPlaceholder: TextView
    private lateinit var btRefresh: Button
    private lateinit var historySearch: FrameLayout
    private lateinit var rvTrackHistory: RecyclerView
    private lateinit var btClearHistory: Button
    private var lastSearchQuery: String? = null

    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edSearch = binding.edSearch
        btClear = binding.btClear
        placeholderView = binding.placeholderView
        rvTrack = binding.rvTrack
        btBackSearch = binding.btBackSearch
        ivPlaceholder = binding.ivPlaceholder
        tvPlaceholder = binding.tvPlaceholder
        btRefresh = binding.btRefresh
        historySearch = binding.historySearch
        rvTrackHistory = binding.rvTrackHistory
        btClearHistory = binding.btClearHistory
        progressBar = binding.progressBarr
        startRvTrack()
        startRvTrackHistory()

        val sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE)

        edSearch.setOnFocusChangeListener { _, hasFocus ->
            val tracks = searchHistory.read(sharedPref)
            recentTracks.clear()
            for (track in tracks)
                recentTracks.add(track)
            recentAdapter.notifyDataSetChanged()
            historySearch.isVisible = hasFocus && edSearch.text.isEmpty() && recentTracks.size != 0
        }

        historySearch.isVisible = false
        

        // Восстановить значение из SharedPreferences, если оно было сохранено
        val sharedPreferences = getSharedPreferences(SEARCH_ACTIVITY, Context.MODE_PRIVATE)
        searchText = sharedPreferences.getString(SEARCH_TEXT, "") ?: ""
        edSearch.setText(searchText)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT, "")
            edSearch.setText(searchText)
        }

        btClear.setOnClickListener {
            edSearch.setText("")
            placeholderView.isVisible = false

            // Скрыть клавиатуру
            val hideKeyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(edSearch.windowToken, 0)

            // Очистить значение из SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString(SEARCH_TEXT, "")
            editor.apply()
            recentAdapter.notifyDataSetChanged()
            btClear.isVisible = false
            rvTrack.isVisible = false
            tvPlaceholder.isVisible = false
            ivPlaceholder.isVisible = false
            btRefresh.isVisible = false
        }

        fun clickOnTrack(track: Track) {
            if (clickDebounce()) {
                val recentSongs: ArrayList<Track> = SearchHistory().read(sharedPref)
                addTrack(track, recentSongs)
                SearchHistory().write(sharedPref, recentSongs)

                val intent = Intent(this, PlayerDisplayActivity::class.java)

                val trackJson = Gson().toJson(track)
                intent.putExtra("LAST_TRACK", trackJson)
                startActivity(intent)
            }
        }

        adapter.itemClickListener = { _, track ->
            clickOnTrack(track)
        }

        recentAdapter.itemClickListener = {_, track ->
            clickOnTrack(track)
        }

        sharedPref.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == HISTORY_KEY) {
                val tracks = SearchHistory().read(sharedPref)
                recentTracks.clear()
                for (track in tracks)
                    recentTracks.add(track)
                recentAdapter.notifyDataSetChanged()
            }
        }

        btClearHistory.setOnClickListener {
            val recentSongs: ArrayList<Track> = SearchHistory().read(sharedPref)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
            recentSongs.clear()
            recentAdapter.notifyDataSetChanged()
            historySearch.isVisible = false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btClear.visibility = View.GONE
                historySearch.visibility = if(edSearch.hasFocus()
                    && s?.isEmpty() == true && recentTracks.size != 0) View.VISIBLE else View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btClear.visibility = clearButtonVisibility(s)
                rvTrack.visibility = if(s?.isEmpty() == true) View.GONE else View.VISIBLE
                historySearch.visibility = if(edSearch.hasFocus()
                    && s?.isEmpty() == true && recentTracks.size != 0) View.VISIBLE else View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        edSearch.addTextChangedListener(simpleTextWatcher)

        btRefresh.setOnClickListener {
            val query = lastSearchQuery
            if (query != null) {
                startSearch(query)
            }
        }

        // Обработка нажатия на кнопку "Назад"
        btBackSearch.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        handler.removeCallbacks(clickRunnable)
    }
    private fun addTrack(track: Track, place : ArrayList<Track>){
        if (place.size == NUMBER_TEN)
            place.removeAt(NUMBER_NINE)
        if (place.contains(track))
            place.remove(track)
        place.add(NUMBER_ZERO, track)
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    private fun startSearch(query: String) {
        if (query.isEmpty()) {
            return  // Если query пустая, просто выходим из функции
        }
        progressBar.isVisible = true
        rvTrack.isVisible = false
        placeholderView.isVisible = false
        // отправляем запрос в itunes
        iTunesService.search(query).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
               progressBar.isVisible = false
                val bodyResponse = response.body()?.results
                if (response.isSuccessful) {
                    trackList.clear()
                    if (bodyResponse?.isNotEmpty() == true) {
                        trackList.addAll(bodyResponse)
                        adapter.notifyDataSetChanged()
                        rvTrack.isVisible = true
                    }
                    val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    if (trackList.isEmpty()) {
                        progressBar.isVisible = false
                        placeholderView.isVisible = true
                        ivPlaceholder.isVisible = true
                        tvPlaceholder.isVisible = true
                        ivPlaceholder.setImageResource(
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                R.drawable.ic_error_faund_track_night
                            } else {
                                R.drawable.ic_error_faund_track
                            }
                        )
                    }
                    btRefresh.isVisible = false
                } else {
                    lastSearchQuery = query
                    placeholderView.isVisible = true
                    showMessage(
                        getString(R.string.error_to_link),
                        response.code().toString()
                    )
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                progressBar.isVisible = false
                lastSearchQuery = query
                placeholderView.isVisible = true
                ivPlaceholder.isVisible = true
                ivPlaceholder.setImageResource(
                    if (isNightModeEnabled()) {
                        R.drawable.ic_error_to_link_night
                    } else {
                        R.drawable.ic_error_to_link
                    }
                )
                showMessage(getString(R.string.error_to_link), t.message.toString())
                btRefresh.isVisible = true
            }

            private fun isNightModeEnabled(): Boolean {
                val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            }
        })
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(clickRunnable, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showMessage(text: String, additionalMessage: String) {

        if (text.isNotEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            tvPlaceholder.isVisible = true
            tvPlaceholder.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            tvPlaceholder.isVisible = false
        }
    }
    private fun startRvTrack() {
        rvTrack.adapter = adapter
        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    private fun startRvTrackHistory() {
        rvTrackHistory.adapter = recentAdapter
        rvTrackHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, edSearch.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        edSearch.setText(searchText)
    }
    override fun onPause() {
        super.onPause()
        // Сохранить значение введенного текста в SharedPreferences
        val sharedPreferences = getSharedPreferences(SEARCH_ACTIVITY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SEARCH_TEXT, searchText)
        editor.apply()
    }
}
