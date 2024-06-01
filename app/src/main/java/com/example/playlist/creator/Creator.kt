package com.example.playlist.creator

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.playlist.data.player.impl.PlayerRepositoryImpl
import com.example.playlist.data.search.impl.SearchHistoryImpl
import com.example.playlist.data.settings.impl.ThemeRepositoryImpl
import com.example.playlist.data.search.network.RetrofitNetworkClient
import com.example.playlist.data.search.network.TrackRepositoryImpl
import com.example.playlist.data.sharing.impl.ExternalNavigator
import com.example.playlist.data.sharing.impl.SharingRepositoryImpl
import com.example.playlist.domain.player.PlayerReposiroty
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.settings.api.ThemeRepository
import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.api.TrackPutRepository
import com.example.playlist.domain.search.api.TrackRepository
import com.example.playlist.domain.search.impl.TrackInteractorImpl
import com.example.playlist.domain.settings.ThemeInteractor
import com.example.playlist.domain.sharing.SharingRepository
import com.example.playlist.domain.sharing.impl.SharingInteractor

@SuppressLint("StaticFieldLeak")
object Creator {
    const val KEY_SP = "Shared_Preferences_Key"
    private lateinit var contextApp : Context
    fun setContext(context: Context)      {
        contextApp = context
    }
    private fun getSharedPref(): SharedPreferences{
        return contextApp.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
    }

    fun provideSharingInteractor(): SharingInteractor{
        return SharingInteractor(provideSharingRepository())
    }

    fun provideSharingRepository(): SharingRepository{
        return SharingRepositoryImpl(context = contextApp, externalNavigator = provideExternalNavigator())
    }

    fun provideExternalNavigator() : ExternalNavigator{
        return ExternalNavigator(contextApp)
    }

    fun providePlayerInteractor(): PlayerReposiroty{
        return PlayerRepositoryImpl()
    }

    fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryImpl(getSharedPref())
    }

    fun provideThemeInteractor(): ThemeInteractor{
        return ThemeInteractor(provideThemeRepository())
    }

    fun provideThemeRepository(): ThemeRepository{
        return ThemeRepositoryImpl(getSharedPref())
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

}