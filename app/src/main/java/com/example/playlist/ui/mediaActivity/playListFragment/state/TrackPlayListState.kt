package com.example.playlist.ui.mediaActivity.playListFragment.state

import com.example.playlist.domain.search.models.Track

sealed class TrackPlayListState() {
    data object EmptyPlaylist : TrackPlayListState()
    class ContentPlaylist(val listTracksPlaylist: MutableList<Track>) : TrackPlayListState()
}