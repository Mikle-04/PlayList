package com.example.playlist.ui.mediaActivity.playListFragment.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.viewModel.CreateAlbumFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditorPlayListViewModel (
    val playlistId: Int,
    private val playlistInteractor: PlayListInteractor,
) : CreateAlbumFragmentViewModel(playlistInteractor) {

    init {
        getPlayListById(playlistId)
    }

    private var playlistState = MutableLiveData<PlayList>()
    fun getPlaylistState(): LiveData<PlayList> = playlistState

    private fun getPlayListById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist(playlistId).collect { playlist ->
                playlistState.postValue(playlist)
            }
        }
    }

    fun saveUpdatePlayList(
        playlist: PlayList,
        namePlaylist: String,
        description: String?,
        uriImageStorage: Uri?,
    ) {
        viewModelScope.launch {
            val editedPlaylist = editPlaylist(
                playlist,
                namePlaylist,
                description,
                uriImageStorage
            )
            playlistInteractor.saveUpdatePlayList(editedPlaylist)
        }
    }

    private fun editPlaylist(
        playlist: PlayList,
        namePlaylist: String,
        description: String?,
        uriImageStorage: Uri?,
    ): PlayList {
        playlist.namePlaylist = namePlaylist
        playlist.descriptionPlaylist = description
        playlist.imgStorage = uriImageStorage
        return playlist
    }
}