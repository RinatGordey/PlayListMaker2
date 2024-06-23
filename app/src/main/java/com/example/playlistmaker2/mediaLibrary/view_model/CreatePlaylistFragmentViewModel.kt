package com.example.playlistmaker2.mediaLibrary.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class CreatePlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    companion object {
        const val MY_IMAGE_PLAYLIST = "my image"
    }

    private var nameImage = ""

    fun createPlaylist(name: String, description: String) {
        val playlist = Playlist(null,name, description, nameImage, "", 0)
        viewModelScope.launch {
            playlistInteractor.addToPlaylist(playlist)
        }
    }

    fun saveImageToStorage(uri: Uri, context: Context) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_IMAGE_PLAYLIST)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        var number= Random.nextInt(1, 100000)
        nameImage ="playlist_cover$number.jpg"
        val file = File(filePath, nameImage)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}