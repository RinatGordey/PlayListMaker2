package com.example.playlistmaker2.util

enum class ErrorType(val message: String) {
    INTERNET("Отсутствует подключение к интернету"),
    SERVER_ERROR("Ошибка сервера"),
}