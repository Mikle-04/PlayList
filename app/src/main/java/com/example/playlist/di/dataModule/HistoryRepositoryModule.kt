package com.example.playlist.di.dataModule

import android.content.Context
import android.content.SharedPreferences
import com.example.playlist.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlist.domain.search.api.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val historySearchRepositoryModule = module {
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory<SharedPreferences> {
        androidContext().getSharedPreferences("SearchHistoryTrack", Context.MODE_PRIVATE)
    }
}