package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistFragment()
    }

    //private val viewModel by viewModel<PlaylistsFragmentViewModel>()
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var textWatcherName: TextWatcher
    private lateinit var textWatcherDescription: TextWatcher
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    var addedImage = false

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

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .transform(RoundedCorners(30))
                        .into(binding.coverImageButton)

                    //binding.coverImageButton.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    binding.placeholderImage.visibility = View.GONE
                    addedImage = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        textWatcherName = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.namePLTextView.visibility = View.GONE
                    binding.namePLEditText.setBackgroundResource(R.drawable.pl_edit_text)
                    binding.createPlaylistButton.setBackgroundResource(R.drawable.item_disabled)

                } else {
                    binding.namePLTextView.visibility = View.VISIBLE
                    binding.namePLEditText.setBackgroundResource(R.drawable.pl_edit_text_input)
                    binding.createPlaylistButton.setBackgroundResource(R.drawable.item_enabled)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcherName?.let { binding.namePLEditText.addTextChangedListener(it) }


        textWatcherDescription = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.descriptionPLTextView.visibility = View.GONE
                    binding.descriptionPLEditText.setBackgroundResource(R.drawable.pl_edit_text)

                } else {
                    binding.descriptionPLTextView.visibility = View.VISIBLE
                    binding.descriptionPLEditText.setBackgroundResource(R.drawable.pl_edit_text_input)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcherDescription?.let { binding.descriptionPLEditText.addTextChangedListener(it) }


        binding.coverImageButton.setOnClickListener {
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

        if (binding.namePLEditText.text.toString().isNotEmpty()
            or binding.descriptionPLEditText.text.toString().isNotEmpty()
            or addedImage
        ) {
            MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNeutralButton("Отмена") { dialog, which ->
                }

                .setNegativeButton("Завершить") { dialog, which ->
                    findNavController().navigateUp()
                }
                .show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
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