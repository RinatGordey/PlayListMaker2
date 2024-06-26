package com.example.playlistmaker2.search.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentSearchBinding
import com.example.playlistmaker2.player.ui.activity.PlayerDisplayFragment
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.models.TrackSearchState
import com.example.playlistmaker2.search.ui.view_model.TrackSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.TrackClickListener {
    private companion object {
        private const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val DELAY_ADD_TRACK_TO_HISTORY = 300L
    private const val TRACK_LIST = "TRACK_LIST"
    }

    private val viewModel by viewModel<TrackSearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true
    private var searchEditTextValue: String = ""
    private val adapter = TrackAdapter(arrayListOf(), this)
    private var textWatcher: TextWatcher? = null
    private var searchResults: MutableList<Track> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val savedTrackList = savedInstanceState?.getParcelableArrayList<Track>(TRACK_LIST)
        if (savedTrackList != null) {
            adapter.trackList = savedTrackList
            adapter.notifyDataSetChanged()
        }

        if (savedInstanceState != null) {
            searchEditTextValue = savedInstanceState.getString(SEARCH_EDITTEXT, "")
            binding.edSearch.setText(searchEditTextValue)
            if (searchEditTextValue.isEmpty()) {
                binding.historySearch.isVisible = false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.rvTrack.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.btClear.setOnClickListener {
            viewModel.getHistory()
            binding.edSearch.setText("")
            adapter.notifyDataSetChanged()

            val view = requireActivity().currentFocus
            if (view != null) {
                hideKeyboard()
                view.run { clearFocus() }
            }
        }

        binding.btClearHistory.setOnClickListener {
            viewModel.cleanHistory()
        }

        binding.edSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (searchEditTextValue.isEmpty()) {
                    viewModel.getHistory()
                }
                binding.historySearch.isVisible = true
            } else {
                binding.historySearch.isVisible = false
            }
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edSearch.hasFocus() && s.isNullOrEmpty()) {
                    viewModel.getHistory()
                }
                binding.btClear.visibility = clearButtonVisibility(binding.edSearch.text, binding.edSearch.hasFocus())
                searchEditTextValue = s?.toString() ?: ""
                viewModel.searchDebounce(
                    searchEditTextValue
                )
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher.let {
            binding.edSearch.addTextChangedListener(it)
        }

        binding.btRefresh.setOnClickListener {
            viewModel.searchRequest(searchEditTextValue)
        }

        searchEditTextValue = savedInstanceState?.getString(SEARCH_EDITTEXT, "") ?: ""
        if (searchEditTextValue.isNotEmpty()) {
            binding.edSearch.setText(searchEditTextValue)
            viewModel.searchRequest(searchEditTextValue)
        }
    }

    override fun onResume() {
        super.onResume()
        if (searchResults.isNotEmpty()) {
            showContent(searchResults)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun clearButtonVisibility(s: CharSequence?, hasFocus: Boolean): Int {
        return if (s.isNullOrEmpty() && hasFocus) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDITTEXT, searchEditTextValue)
        outState.putParcelableArrayList(TRACK_LIST, adapter.trackList as ArrayList<out Parcelable>)
    }

    private fun render(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.SearchContent -> { searchResults = state.trackInfo
                showContent(searchResults)
            }
            is TrackSearchState.Empty -> showEmpty(state.message)
            is TrackSearchState.Error -> showError(state.errorMessage)
            is TrackSearchState.Loading -> showLoading()
            is TrackSearchState.HistoryContent -> showHistory(state.trackInfo)
        }
    }

    private fun showContent(tracks: MutableList<Track>) {
        binding.apply {
            historySearch.isVisible = true
            placeholderView.isVisible = false
            progressBar.isVisible = false
            rvTrack.isVisible = true
            btClearHistory.isVisible = false
            tvYouSearch.isVisible = false
            rvTrack.adapter = adapter
            adapter.trackList = tracks as ArrayList<Track>
            adapter.notifyDataSetChanged()
        }
    }

    private fun showEmpty(message: String) {
        binding.apply {
            placeholderView.isVisible = true
            ivPlaceholderNothingFound.isVisible = true
            ivPlaceholderNoInternet.isVisible = false
            tvPlaceholderNoInternet.isVisible = false
            btRefresh.isVisible = false
            progressBar.isVisible = false
            tvPlaceholderNothingFound.isVisible = true
            rvTrack.isVisible = false
            btClearHistory.isVisible = false
            tvYouSearch.isVisible = false
            tvPlaceholderNothingFound.text = message
        }
    }

    private fun showError(message: String) {
        binding.apply {
            placeholderView.isVisible = true
            ivPlaceholderNothingFound.isVisible = false
            ivPlaceholderNoInternet.isVisible = true
            tvPlaceholderNoInternet.isVisible = true
            btRefresh.isVisible = true
            progressBar.isVisible = false
            tvPlaceholderNothingFound.isVisible = false
            rvTrack.isVisible = false
            btClearHistory.isVisible = false
            tvYouSearch.isVisible = false
            tvPlaceholderNoInternet.text = message
        }
    }

    private fun showLoading() {
        binding.apply {
            ivPlaceholderNothingFound.isVisible = false
            ivPlaceholderNoInternet.isVisible = false
            tvPlaceholderNoInternet.isVisible = false
            btRefresh.isVisible = false
            progressBar.isVisible = true
            tvPlaceholderNothingFound.isVisible = false
            rvTrack.isVisible = false
            btClearHistory.isVisible = false
            tvYouSearch.isVisible = false
        }
    }

    private fun showHistory(tracks: MutableList<Track>) {
        binding.apply {
            placeholderView.isVisible = false
            ivPlaceholderNothingFound.isVisible = false
            ivPlaceholderNoInternet.isVisible = false
            tvPlaceholderNoInternet.isVisible = false
            btRefresh.isVisible = false
            progressBar.isVisible = false
            tvPlaceholderNothingFound.isVisible = false
            rvTrack.isVisible = true
            if (tracks.isEmpty()) {
                btClearHistory.isVisible = false
                tvYouSearch.isVisible = false
            } else {
                btClearHistory.isVisible = true
                tvYouSearch.isVisible = true
            }
            rvTrack.adapter = adapter
            adapter.trackList = tracks as ArrayList<Track>
            adapter.notifyDataSetChanged()
        }
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            isClickAllowed = true
            findNavController().navigate(R.id.action_searchFragment_to_playerDisplayFragment,
                PlayerDisplayFragment.createArgs(track))
        lifecycleScope.launch {
            delay(DELAY_ADD_TRACK_TO_HISTORY)
            viewModel.historyAddTrack(track)
        }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}