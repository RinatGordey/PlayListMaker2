package com.example.playlistmaker2.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.player.domain.models.PlayerState
import com.example.playlistmaker2.player.ui.mapper.TrackMapper
import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.network.ITunesAPI
import com.example.playlistmaker2.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HISTORY_PREFERENCES = "history_preferences"
const val THEME_APP_PREFERENCES = "Theme_app_preferences"

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<SharedPreferences>(named(HISTORY_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single(named(THEME_APP_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(THEME_APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { MediaPlayer() }

    single { PlayerState.DEFAULT }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single { TrackMapper() }
}