package com.example.playlistmaker2.player.ui.model

import java.io.Serializable

data class TrackInfo(
    val trackId: Int, // Идентификатор треков с сервера iTunes
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String, // Передаёт ссылку на отрывок трека
): Serializable