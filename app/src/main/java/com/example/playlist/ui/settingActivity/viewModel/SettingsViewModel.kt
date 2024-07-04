package com.example.playlist.ui.settingActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist.domain.settings.ThemeInteractor
import com.example.playlist.domain.sharing.impl.SharingInteractor
import org.koin.core.component.KoinComponent


class SettingsViewModel(
    val sharingInteractor: SharingInteractor,
    val themeInteractor: ThemeInteractor
) : ViewModel(), KoinComponent {


    private var themeLiveData = MutableLiveData<Boolean>()
    val observeTheme: LiveData<Boolean> = themeLiveData
    init {
        getTheme()
    }



    fun switchTheme(theme: Boolean) {
        themeInteractor.updateThemeSettings(theme)
        themeLiveData.value = theme
    }

    private fun getTheme() {
        themeLiveData.value = themeInteractor.getThemeSettings()
    }

    fun shareLink() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openEmail()
    }

    fun openTerm() {
        sharingInteractor.openTerms()
    }
}