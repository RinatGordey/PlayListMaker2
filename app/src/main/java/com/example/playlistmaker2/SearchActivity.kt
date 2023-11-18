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

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.btBackSearch)
        backButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("searchText", searchText)
            setResult(Activity.RESULT_OK, returnIntent)
            finish() // Закрываем эту активити при нажатии "назад"
        }

        searchEditText = findViewById(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.btClear)

        // Восстановить значение из SharedPreferences, если оно было сохранено
        val sharedPreferences = getSharedPreferences("SearchActivity", Context.MODE_PRIVATE)
        searchText = sharedPreferences.getString("searchText", "") ?: ""
        searchEditText.setText(searchText)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("searchText", "")
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
            editor.putString("searchText", "")
            editor.apply()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchText", searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText", "")
        searchEditText.setText(searchText)
    }

    override fun onPause() {
        super.onPause()

        // Сохранить значение введенного текста в SharedPreferences
        val sharedPreferences = getSharedPreferences("SearchActivity", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("searchText", searchText)
        editor.apply()
    }
}