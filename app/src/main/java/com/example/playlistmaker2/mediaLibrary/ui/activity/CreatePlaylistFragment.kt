package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.mediaLibrary.view_model.CreatePlaylistFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    companion object {
        const val MY_ALBUM = "myAlbum"
        const val COVER = "cover"
        const val PLAYLIST = "Плейлист"
        const val CREATED = "создан"
    }

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private var textWatcherName: TextWatcher? = null
    private var textWatcherDescription: TextWatcher? = null
    private var addedImage = false
    private val viewModel by viewModel<CreatePlaylistFragmentViewModel>()
    private var uriDb: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    uriDb = uri.toString()
                    binding.btCoverImage.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    binding.imPlaceholder.isVisible = false
                    addedImage = true
                } else {}
            }

        textWatcherName = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    binding.apply {
                        tvNamePlaylist.isVisible = false
                        edNamePlaylist.setBackgroundResource(R.drawable.ed_playlist)
                        btCreatePlaylist.setBackgroundResource(R.drawable.item_disabled)
                    }

                } else {
                    binding.apply {
                        tvNamePlaylist.isVisible = true
                        edNamePlaylist.setBackgroundResource(R.drawable.ed_playlist_input)
                        btCreatePlaylist.setBackgroundResource(R.drawable.item_enabled)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        textWatcherName.let { binding.edNamePlaylist.addTextChangedListener(it) }


        textWatcherDescription = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    binding.apply {
                        tvDescriptionPlaylist.isVisible = false
                        edDescriptionPlaylist.setBackgroundResource(R.drawable.ed_playlist)
                    }

                } else {
                    binding.apply {
                        tvDescriptionPlaylist.isVisible = true
                        edDescriptionPlaylist.setBackgroundResource(R.drawable.ed_playlist_input)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        textWatcherDescription.let { binding.edDescriptionPlaylist.addTextChangedListener(it) }

        binding.btCreatePlaylist.setOnClickListener {
            viewModel.createPlaylist(
                Playlist(
                    playlistId = null,
                    playlistName = binding.edNamePlaylist.text.toString(),
                    playlistDescription = binding.edDescriptionPlaylist.text.toString(),
                    uri =uriDb,
                )
            )

            Toast.makeText(requireContext(), PLAYLIST + " " + binding.edNamePlaylist.text.toString()
                    + " " + CREATED, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.btCoverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btBackArrow.setOnClickListener {
            showDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        })
    }

    private fun showDialog() {
        if (binding.edNamePlaylist.text.toString().isNotEmpty()
            or binding.edDescriptionPlaylist.text.toString().isNotEmpty()
            or addedImage
        ) {
            MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
                .setTitle(R.string.finish_creating_a_playlist)
                .setMessage(R.string.all_unsaved_data_will_be_lost)
                .setNeutralButton(R.string.cancel) { _, _ ->
                }
                .setNegativeButton(R.string.complete) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_ALBUM)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, COVER)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}