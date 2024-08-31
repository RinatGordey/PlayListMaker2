package com.example.playlistmaker2.mediaLibrary.ui.activity

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
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.mediaLibrary.view_model.CreatePlaylistFragmentViewModel.Companion.MY_IMAGE_PLAYLIST
import com.example.playlistmaker2.mediaLibrary.view_model.EditPlaylistViewModel
import com.example.playlistmaker2.util.ConversionDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditPlaylistFragment : CreatePlaylistFragment() {

    private var currentPlaylist: Playlist? = null
    private var callback: OnBackPressedCallback? = null
    private var newUri = false
    private var uriDb: String? = null

    override val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btCreatePlaylist.text = getString(R.string.save)
        binding.tvNewPlaylist.text = getString(R.string.edit)
        currentPlaylist = requireArguments().getSerializable(PLAYLIST) as Playlist
        binding.edNamePlaylist.setText(currentPlaylist!!.playlistName)
        binding.edDescriptionPlaylist.setText(currentPlaylist!!.playlistDescription)
        val uri = getImage(currentPlaylist!!)

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

        if (currentPlaylist!!.uri != null) {
            binding.imPlaceholder.isVisible = false
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.ic_stub2)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(binding.btCoverImage)
        }

        binding.btCoverImage.setOnClickListener {
            newUri = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.edNamePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btCreatePlaylist.isEnabled = s?.isNotEmpty() == true
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btCreatePlaylist.setOnClickListener {
            if (binding.edNamePlaylist.text.isNotEmpty()) {
                if ((binding.edNamePlaylist.text.toString() != currentPlaylist!!.playlistName)
                    || (binding.edDescriptionPlaylist.text.toString() != currentPlaylist!!.playlistDescription)
                    || (newUri)) {

                    viewModel.update(
                        Playlist(
                            currentPlaylist!!.playlistId,
                            binding.edNamePlaylist.text.toString(),
                            binding.edDescriptionPlaylist.text.toString(),
                            currentPlaylist!!.uri,
                            currentPlaylist!!.tracksId,
                            currentPlaylist!!.tracksCount
                        )
                    )
                }

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), R.string.Field_name_is_not_filled_in, Toast.LENGTH_SHORT).show()
            }
        }

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback as OnBackPressedCallback)

        binding.btBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getImage(playlist: Playlist): Uri? {
        val uri: Uri?
        if (playlist.uri != null) {
            val filePath =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_IMAGE_PLAYLIST)
            val file = playlist.uri?.let { File(filePath, it) }
            uri = file!!.toUri()
        } else {
            uri = null
        }
        return uri
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback?.remove()
        _binding = null
    }

    companion object {
       const val PLAYLIST = "Playlist"

        fun createArgs(playlist: Playlist): Bundle = bundleOf(PLAYLIST to playlist)
    }
}