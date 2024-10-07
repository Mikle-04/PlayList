package com.example.playlist.di.dataModule

import com.example.playlist.data.favourite.db.converters.TrackDbConverter
import org.koin.dsl.module

val dataDbConverterModule = module {
    factory { TrackDbConverter() }
}