package com.example.playlistmaker2.di

import com.example.playlistmaker2.db.presentation.FavoriteViewModel
import com.example.playlistmaker2.mediaLibrary.view_model.FavoriteTracksFragmentViewModel
import com.example.playlistmaker2.mediaLibrary.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.player.ui.view_model.PlayerDisplayViewModel
import com.example.playlistmaker2.search.ui.view_model.TrackSearchViewModel
import com.example.playlistmaker2.settings.ui.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{

    viewModel {(playerTrack: TrackInfo) ->
        PlayerDisplayViewModel(playerTrack,get())
    }

    viewModel{
        TrackSearchViewModel(get(),get(),get())
    }

    viewModel{
        SettingsViewModel(get(),get(),get())
    }

    viewModel{
        FavoriteTracksFragmentViewModel()
    }

    viewModel{
        PlaylistFragmentViewModel()
    }

    viewModel {
        FavoriteViewModel(androidContext(), get())
    }
}