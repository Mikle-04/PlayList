package com.example.playlist.ui.mediaActivity.playListFragment.state

import com.example.playlist.domain.search.models.Track

sealed class TrackPlayListState() {
    object EmptyPlaylist : TrackPlayListState()
    class ContentPlaylist(val listTracksOfPlaylist: MutableList<Track>) : TrackPlayListState()
}