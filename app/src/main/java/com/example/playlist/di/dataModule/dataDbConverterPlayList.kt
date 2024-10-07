package com.example.playlist.di.dataModule

import com.example.playlist.data.playList.converter.PlayListConverter
import com.google.gson.Gson
import org.koin.dsl.module

val dataDbConverterPlayList = module {
    factory { PlayListConverter(get()) }

    factory { Gson() }

}