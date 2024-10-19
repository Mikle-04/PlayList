package com.example.playlist.domain.playList.models

import android.net.Uri

data class PlayList(
    var id: Int = 0,
    var namePlaylist: String,
    var descriptionPlaylist: String?,
    var imgStorage: Uri?,
    var listTrackIds: MutableList<Int> = mutableListOf(),
    var amountTracks: Int = 0,
    var totalPlaylistTime: Int = 0,
    var trackSpelling: String = "треков",
    var minutesSpelling: String = "минут",
)