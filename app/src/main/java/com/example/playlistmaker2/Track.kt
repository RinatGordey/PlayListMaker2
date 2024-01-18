package com.example.playlistmaker2

import com.google.gson.annotations.SerializedName

data class Track (
    val trackId: Int, // Идентификатор треков с сервера iTunes
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
)