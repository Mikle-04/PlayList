package com.example.playlist.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.playlist.data.impl.SearchHistoryImpl
import com.example.playlist.data.impl.ThemeRepositoryImpl
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.network.TrackRepositoryImpl
import com.example.playlist.domain.api.SearchHistoryRepository
import com.example.playlist.domain.api.ThemeRepository
import com.example.playlist.domain.api.TrackInteractor
import com.example.playlist.domain.api.TrackRepository
import com.example.playlist.domain.impl.TrackInteractorImpl
import com.example.playlist.presentation.play.PlayTrackPresenter
import com.example.playlist.presentation.tracks.TrackSearchPresenter
import com.example.playlist.presentation.tracks.TrackView

@SuppressLint("StaticFieldLeak")
object Creator {
    const val KEY_SP = "Shared_Preferences_Key"
    private lateinit var contextApp : Context

    fun providePlayController(activity: Activity): PlayTrackPresenter {
        return PlayTrackPresenter(activity)
    }

    fun provideTrackSearchPresenter(context: Context): TrackSearchPresenter {
        return TrackSearchPresenter(context)
    }
    fun provideSearchHistoryRepository():SearchHistoryRepository{
        return SearchHistoryImpl(getSharedPref())
    }

    fun provideThemeRepository(): ThemeRepository{
       return ThemeRepositoryImpl(getSharedPref())
    }

    fun setContext(context: Context)      {
        contextApp = context
    }
    private fun getSharedPref(): SharedPreferences{
      return contextApp.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
    }
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

}