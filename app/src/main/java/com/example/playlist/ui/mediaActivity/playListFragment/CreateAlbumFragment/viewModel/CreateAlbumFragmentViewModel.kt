package com.example.playlist.ui.mediaActivity.playListFragment.CreateAlbumFragment.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import kotlinx.coroutines.launch

class CreateAlbumFragmentViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    fun createPlaylist(name: String, description: String?, uriImage: Uri?) {
        viewModelScope.launch {
            val playlist = playlist(name, description, uriImage)
            playListInteractor.insertPlaylist(playlist)
        }
    }

    private fun playlist(name: String, description: String?, uriImage: Uri?): PlayList = PlayList(
        namePlaylist = name,
        descriptionPlaylist = description,
        imgStorage = uriImage
    )
}