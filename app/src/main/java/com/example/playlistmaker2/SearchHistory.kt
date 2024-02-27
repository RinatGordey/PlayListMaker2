package com.example.playlistmaker2
import android.content.SharedPreferences
import com.example.playlistmaker2.search.ui.SearchActivity.Companion.HISTORY_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class SearchHistory {
    fun read(pref: SharedPreferences): ArrayList<Track> {
        val json = pref.getString(HISTORY_KEY, null) ?: return ArrayList()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
    fun write(pref : SharedPreferences, tracks: ArrayList<Track>){
        val json = Gson().toJson(tracks)
        pref.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}