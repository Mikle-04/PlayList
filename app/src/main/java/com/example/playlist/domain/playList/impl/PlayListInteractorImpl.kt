package com.example.playlist.domain.playList.impl

import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListInteractor
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor{
    override suspend fun insertPlaylist(playlist: PlayList) {
        playListRepository.insertPlayList(playlist)
    }

    override suspend fun insertTrackToPlaylist(track: Track): Flow<Long> {
        return playListRepository.insertTrackToPlaylist(track)
    }
    override suspend fun getListPlaylist(): Flow<MutableList<PlayList>> {
        return playListRepository.getListPlaylist()
    }

    override suspend fun getPlaylist(playlistId: Int): Flow<PlayList> {
        return playListRepository.getPlaylist(playlistId)
    }

    override suspend fun getTracksByPlaylistId(playlistId: Int): Flow<MutableList<Track>> {
        return playListRepository.getTracksByPlaylistId(playlistId)
    }

    override suspend fun deleteSelectedTrackFromPlaylist(track: Track) {
        playListRepository.deleteSelectedTrackFromPlaylist(track)
    }
    override suspend fun deletePlaylistById(playlistId: Int): Flow<Int> {
        return playListRepository.deletePlaylistById(playlistId)
    }

    override suspend fun saveUpdatePlayList(playlist: PlayList) {
        playListRepository.saveUpdatePlayList(playlist)
    }
}