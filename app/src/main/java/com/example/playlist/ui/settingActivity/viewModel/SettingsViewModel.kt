package com.example.playlist.ui.settingActivity.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist.creator.Creator
import com.example.playlist.domain.settings.ThemeInteractor
import com.example.playlist.domain.sharing.impl.SharingInteractor


class SettingsViewModel(
    application: Application,
    val sharingInteractor: SharingInteractor,
    val themeInteractor: ThemeInteractor
) : AndroidViewModel(application) {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                SettingsViewModel(
                    application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application,
                    sharingInteractor = Creator.provideSharingInteractor(),
                    themeInteractor = Creator.provideThemeInteractor()
                )
            }
        }
    }

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