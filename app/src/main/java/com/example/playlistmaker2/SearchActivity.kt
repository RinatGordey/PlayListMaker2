package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmake2.ItunesService
import com.example.playlistmaker2.databinding.ActivitySearchBinding
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
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunseService = retrofit.create(ItunesService::class.java)

    private val trackList = mutableListOf<Track>()


    private lateinit var edSearch: EditText
    private lateinit var placeholderView: LinearLayout
    private lateinit var adapter: TrackAdapter
    private lateinit var btClear: ImageButton
    private lateinit var rvTrack: RecyclerView
    private lateinit var btBackSearch: ImageView
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvPlaceholder: TextView
    private lateinit var btRefresh: Button
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
        startRvTrack()


        btBackSearch.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(SEARCH_TEXT, searchText)
            setResult(Activity.RESULT_OK, returnIntent)
            finish() // Закрываем эту активити при нажатии "назад"
        }

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

            // Скрыть клавиатуру
            val hideKeyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(edSearch.windowToken, 0)

            // Очистить значение из SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString(SEARCH_TEXT, "")
            editor.apply()
            btClear.isVisible = false
            rvTrack.isVisible = false
            tvPlaceholder.isVisible = false
            ivPlaceholder.isVisible = false
            btRefresh.isVisible = false
        }

        edSearch.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                if (charSequence != null) {
                    btClear.isVisible = charSequence.isNotEmpty()  // Показываем кнопку очистки, если есть текст
                }
                searchText = charSequence.toString()  // Обновляем значение searchText
            }
        )

        edSearch.setOnClickListener {
            edSearch.text.clear() // Очищаем текст при нажатии на поле ввода

        }
        btRefresh.setOnClickListener {
            val query = lastSearchQuery
            if (query != null) {
                startSearch(query)
            }
        }
        edSearch.setOnEditorActionListener { _, actionId, _ ->
            val input = edSearch.text.toString()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch(input)
                lastSearchQuery = input

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edSearch.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        // Обработка нажатия на кнопку "Назад"
        btBackSearch.setOnClickListener {
            finish()
        }
    }

    private fun startSearch(query: String) {
        // отправляем запрос в itunes
        iTunseService.search(query).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
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
                        ivPlaceholder.isVisible = true
                        ivPlaceholder.setImageResource(
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                R.drawable.ic_error_faund_track_night
                            } else {
                                R.drawable.ic_error_faund_track
                            }
                        )
                        showMessage(getString(R.string.error_tracks), "")
                    } else {
                        showMessage("", "")
                    }
                    btRefresh.isVisible = false
                } else {
                    lastSearchQuery = query
                    ivPlaceholder.isVisible = true
                    showMessage(
                        getString(R.string.error_to_link),
                        response.code().toString()
                    )
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                lastSearchQuery = query
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

    private fun showMessage(text: String, additionalMessage: String) {

        if (text.isNotEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            tvPlaceholder.isVisible = true
            tvPlaceholder.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            tvPlaceholder.isVisible = false
        }
    }

    private fun startRvTrack() {
        adapter = TrackAdapter(trackList)
        rvTrack.layoutManager = LinearLayoutManager(this)
        rvTrack.adapter = adapter
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