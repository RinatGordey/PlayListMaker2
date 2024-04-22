package com.example.playlistmaker2.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.databinding.FragmentSettingsBinding
import com.example.playlistmaker2.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeSwitcherLiveData().observe(viewLifecycleOwner) { darkThemeEnabled ->
            binding.icSwitch.isChecked = darkThemeEnabled }

        binding.icSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.icShare.setOnClickListener {
            viewModel.shareApp()
        }


        binding.icAgreement.setOnClickListener {
            viewModel.openTerms()
        }

        binding.icSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}