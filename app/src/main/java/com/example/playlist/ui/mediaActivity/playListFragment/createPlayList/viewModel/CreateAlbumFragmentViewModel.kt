package com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import kotlinx.coroutines.launch

open class CreateAlbumFragmentViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    fun createPlaylist(name: String, description: String?, uriImage: Uri?) {
        viewModelScope.launch {
            val playlist = newPlaylist(name, description, uriImage)
            playListInteractor.insertPlaylist(playlist)
        }
    }

    private fun newPlaylist(name: String, description: String?, uriImage: Uri?): PlayList = PlayList(
        namePlaylist = name,
        descriptionPlaylist = description,
        imgStorage = uriImage
    )
}