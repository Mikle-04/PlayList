package com.example.playlist.di.dataModule

import androidx.room.Room
import com.example.playlist.data.favourite.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataDbModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}