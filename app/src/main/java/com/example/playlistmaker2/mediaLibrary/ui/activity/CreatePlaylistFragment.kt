package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker2.mediaLibrary.view_model.CreatePlaylistFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {

    var _binding: FragmentCreatePlaylistBinding? = null
    val binding get() = _binding!!
    private var textWatcherName: TextWatcher? = null
    private var textWatcherDescription: TextWatcher? = null
    var addedImage = false
    open val viewModel by viewModel<CreatePlaylistFragmentViewModel>()
    private var uriDb: String? = null
    private var callback: OnBackPressedCallback? = null

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
                    viewModel.saveImageToPrivateStorage(uri, requireContext())
                    binding.imPlaceholder.isVisible = false
                    addedImage = true
                }
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
            if (binding.edNamePlaylist.text.isNotEmpty()) {
                viewModel.run {
                    createPlaylist(
                        binding.edNamePlaylist.text.toString(),
                        binding.edDescriptionPlaylist.text.toString(),
                    )

                    Toast.makeText(
                        requireContext(), PLAYLIST + " " + binding.edNamePlaylist.text.toString()
                                + " " + CREATED, Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(requireContext(), R.string.Field_name_is_not_filled_in, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btCoverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btBackArrow.setOnClickListener {
            showDialog()
        }

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback as OnBackPressedCallback)
    }

    private fun showDialog() {
        if (_binding != null) {
            val binding = _binding!!
            if (binding.edNamePlaylist.text.toString().isNotEmpty()
                or binding.edDescriptionPlaylist.text.toString().isNotEmpty()
                or addedImage
            ) {
                val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.customDialog)
                    .setTitle(R.string.finish_creating_a_playlist)
                    .setMessage(R.string.all_unsaved_data_will_be_lost)
                    .setNeutralButton(R.string.cancel) { _, _ ->
                    }
                    .setNegativeButton(R.string.complete) { _, _ ->
                        findNavController().popBackStack()
                    }
                    .create()
                dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.main_blue))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.main_blue))
                }
                dialog.show()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback?.remove()
    }

    companion object {
        const val PLAYLIST = "Плейлист"
        const val CREATED = "создан"
    }
}