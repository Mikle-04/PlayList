package com.example.playlist.di.dataModule

import com.example.playlist.data.selectedTrack.convertor.SelectTrackDbConverter
import org.koin.dsl.module

val selectTrackDbConverterModule = module {
    factory { SelectTrackDbConverter() }
}