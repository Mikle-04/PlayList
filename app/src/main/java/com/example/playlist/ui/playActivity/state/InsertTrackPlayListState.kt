package com.example.playlist.ui.playActivity.state

sealed class InsertTrackPlayListState(val namePlaylist: String) {
    class Success(namePlaylist: String) : InsertTrackPlayListState(namePlaylist)
    class Fail(namePlaylist: String) : InsertTrackPlayListState(namePlaylist)
}