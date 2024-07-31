package com.example.playlist.ui.settingActivity

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.playlist.R
import com.example.playlist.databinding.SettingsFragmentBinding
import com.example.playlist.ui.settingActivity.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSettings : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTheme.observe(requireActivity()) {
            switchTheme(it)
        }


        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.imgShare.setOnClickListener {
            viewModel.shareLink()
        }

        binding.imgMode.setOnClickListener {
            viewModel.openSupport()
        }

        binding.imgAgreementNext.setOnClickListener {
            viewModel.openTerm()
        }
    }


    private fun switchTheme(theme: Boolean) {
        binding.switchTheme.isChecked = theme
    }


}