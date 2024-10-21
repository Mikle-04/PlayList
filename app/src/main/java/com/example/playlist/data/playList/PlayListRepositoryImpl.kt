package com.example.playlist.data.playList

import android.content.Context
import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.data.playList.converter.PlayListConverter
import com.example.playlist.data.playList.db.PlayListEntity
import com.example.playlist.data.playList.selectedTrack.convertor.SelectTrackDbConverter
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.playList.models.SelectTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converterPlayList: PlayListConverter,
    private val selectTrackDbConverter: SelectTrackDbConverter,
    private val playListConverter: PlayListConverter,
    private val context: Context
) : PlayListRepository {
    override suspend fun insertPlayList(playList: PlayList) {
        val playListEntity = converterPlayList.mapPlayListToEntity(playList)
        appDatabase.playListDao().insertPlaylist(playListEntity)
    }



    override suspend fun insertTrackToPlaylist(selectedTrack: SelectTrack): Flow<Long> = flow {
        val trackInPlaylist = appDatabase.selectedTrackDao()
            .getTrackIdOfPlaylistByPlaylistId(selectedTrack.playlistId)
        if (trackInPlaylist.contains(selectedTrack.trackId)) {
            emit(TRACK_INSERT)
        } else {
            val selectTrackEntity = selectTrackDbConverter.mapModelToEntity(selectedTrack)
            appDatabase.selectedTrackDao().insertTrackToPlaylist(selectTrackEntity)
            updatePlaylist(selectedTrack)
            emit(SUCCESS_INSERT)
        }
    }

    private suspend fun updatePlaylist(selectedTrack: SelectTrack) {

        val playlistEntity = appDatabase.playListDao().getPlaylistById(selectedTrack.playlistId)
        val playlist = converterPlayList.mapEntityToPlayList(playlistEntity)

        val listTrackIds = appDatabase.selectedTrackDao()
            .getTrackIdOfPlaylistByPlaylistId(selectedTrack.playlistId)
        val amountTracks = listTrackIds.size

        playlist.listTrackIds = listTrackIds
        playlist.amountTracks = amountTracks

        val playlistEntityOut = converterPlayList.mapPlayListToEntity(playlist)
        appDatabase.playListDao().updatePlaylist(playlistEntityOut)
    }

    override suspend fun getPlaylist(): Flow<MutableList<PlayList>> = flow {
        val playlistEntity = appDatabase.playListDao().getPlaylists()
        emit(convertPlayListEntityToPlayList(playlistEntity))
    }

    private fun convertPlayListEntityToPlayList(playlists: MutableList<PlayListEntity>): MutableList<PlayList> {
        return playlists.map { playlistEntity ->
            converterPlayList.mapEntityToPlayList(playlistEntity)
        }.toMutableList()
    }



    companion object {
        const val SUCCESS_INSERT = 1L
        const val TRACK_INSERT = 0L
    }
}