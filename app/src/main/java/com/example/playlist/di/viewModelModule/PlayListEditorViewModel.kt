package com.example.playlist.di.viewModelModule

import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.EditorPlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playListEditorViewModel = module {
    viewModel{(playlistId: Int) ->
        EditorPlayListViewModel(
            playlistId,
            get()
        )}
}