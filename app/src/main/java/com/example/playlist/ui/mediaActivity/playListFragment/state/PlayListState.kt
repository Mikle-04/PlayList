package com.example.playlist.ui.mediaActivity.playListFragment.state

import com.example.playlist.domain.playList.models.PlayList

sealed class PlayListState(val playlists: MutableList<PlayList>) {
    class Content(playlists: MutableList<PlayList>): PlayListState(playlists)
    class Empty(): PlayListState(emptyList<PlayList>().toMutableList())
}