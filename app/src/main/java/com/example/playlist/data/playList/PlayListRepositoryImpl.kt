package com.example.playlist.data.playList

import android.content.Context
import com.example.playlist.R
import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.data.playList.converter.PlayListConverter
import com.example.playlist.data.playList.db.PlayListEntity
import com.example.playlist.data.playList.selectedTrack.convertor.SelectTrackDbConverter
import com.example.playlist.data.playList.selectedTrack.db.SelectedTrackEntity
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.playList.models.SelectTrack
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

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

    override suspend fun insertTrackToPlaylist(track: Track): Flow<Long> = flow {
        val trackIdsOfPlaylist =
            appDatabase.selectedTrackDao().getTrackIdOfPlaylistByPlaylistId(track.playlistId)
        if (trackIdsOfPlaylist.contains(track.trackId)) {
            emit(TRACK_INSERT)
        } else {
            val selectedTrackEntity = selectTrackDbConverter.mapModelToEntity(track)
            appDatabase.selectedTrackDao().insertTrackToPlaylist(selectedTrackEntity)

            updatePlaylist(track)

            emit(SUCCESS_INSERT)
        }
    }

    private suspend fun updatePlaylist(track: Track) {

        val playlistEntity = appDatabase.playListDao().getPlaylistById(track.playlistId)
        val playlist = playListConverter.mapEntityToPlayList(playlistEntity)

        val listTrackIds = appDatabase.selectedTrackDao().getTrackIdOfPlaylistByPlaylistId(track.playlistId)
        val amountTracks = listTrackIds.size

        val listTracksTime = appDatabase.selectedTrackDao().getListTrackTimeOfPlaylistById(track.playlistId)
        val totalTimePlaylist = listTracksTime.sum()

        playlist.listTrackIds = listTrackIds
        playlist.amountTracks = amountTracks
        playlist.trackSpelling = chooseSpellingTrack(amountTracks)
        playlist.totalPlaylistTime = totalTimePlaylist
        playlist.minutesSpelling = chooseSpellingMinutes(playlist.totalPlaylistTime)

        val playlistEntityNew = playListConverter.mapPlayListToEntity(playlist)
        appDatabase.playListDao().updatePlaylist(playlistEntityNew)
    }

    override suspend fun getListPlaylist(): Flow<MutableList<PlayList>> = flow {
        val playlistEntity = appDatabase.playListDao().getPlaylists()
        emit(convertPlayListEntityToPlayList(playlistEntity))
    }

    override suspend fun getPlaylist(playlistId: Int): Flow<PlayList> = flow {
        val playlistEntity = appDatabase.playListDao().getPlaylistById(playlistId)
        emit(playListConverter.mapEntityToPlayList(playlistEntity))
    }

    override suspend fun getTracksByPlaylistId(playlistId: Int): Flow<MutableList<Track>> = flow {
        val listTracksOfPlaylistEntity =
            appDatabase.selectedTrackDao().getTracksByPlaylistId(playlistId)
        emit(convertListOfTracksEntityToModel(listTracksOfPlaylistEntity))
    }
    override suspend fun deleteSelectedTrackFromPlaylist(track: Track) {
        val deletedTrackEntity = selectTrackDbConverter.mapModelToEntity(track)
        appDatabase.selectedTrackDao().deleteSelectedTrackFromPlaylist(deletedTrackEntity)
        updatePlaylist(track)
    }
    override suspend fun deletePlaylistById(playlistId: Int): Flow<Int> = flow {
        val playlistEntity = appDatabase.playListDao().getPlaylistById(playlistId)
        appDatabase.playListDao().deletePlaylist(playlistEntity)
        emit(DELETE)
    }

    private fun convertPlayListEntityToPlayList(playlists: MutableList<PlayListEntity>): MutableList<PlayList> {
        return playlists.map { playlistEntity ->
            converterPlayList.mapEntityToPlayList(playlistEntity)
        }.toMutableList()
    }

    private fun convertListOfTracksEntityToModel(listTracksEntity: MutableList<SelectedTrackEntity>): MutableList<Track> {
        return listTracksEntity.map { selectedTrackEntity ->
            selectTrackDbConverter.mapEntityToModel(selectedTrackEntity)
        }.toMutableList()
    }

    private fun chooseSpellingTrack(amountTracks: Int): String {
        return if (amountTracks % 100 in 11..19) {
            context.getString(R.string.tracks)
        } else {
            when (amountTracks % 10) {
                1 -> context.getString(R.string.track)
                2, 3, 4 -> context.getString(R.string.track_a)
                else -> context.getString(R.string.tracks)
            }
        }
    }

    private fun chooseSpellingMinutes(totalPlaylistTime: Int): String {
        val timeInMinutes = totalPlaylistTime.milliseconds.inWholeMinutes
        return if (timeInMinutes % 100 in 11..19) {
            context.getString(R.string.minut)
        } else {
            when (timeInMinutes % 10) {
                1L -> context.getString(R.string.minuta)
                2L, 3L, 4L -> context.getString(R.string.minuti)
                else -> context.getString(R.string.minut)
            }
        }
    }

    companion object {
        const val SUCCESS_INSERT = 1L
        const val TRACK_INSERT = 0L
        const val DELETE = 1
    }
}



