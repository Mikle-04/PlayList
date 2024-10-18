package com.example.playlist.data.playList

import android.content.Context
import com.example.playlist.R
import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.data.playList.converter.PlayListConverter
import com.example.playlist.data.playList.db.PlayListEntity
import com.example.playlist.data.selectedTrack.convertor.SelectTrackDbConverter
import com.example.playlist.data.selectedTrack.db.SelectedTrackEntity
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListRepository
import com.example.playlist.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListConverter,
    private val selectTrackDbConverter: SelectTrackDbConverter,
    private val playListConverter: PlayListConverter,
    private val context: Context
) : PlayListRepository {
    override suspend fun insertPlayList(playList: PlayList) {
        val playListEntity = converter.mapPlayListEntityToPlayList(playList)
        appDatabase.playListDao().insertPlaylist(playListEntity)
    }

    override suspend fun getListPlayList(): Flow<MutableList<PlayList>> = flow {
        val playLists = appDatabase.playListDao().getPlaylists()
        emit(convertPlayListEntityToPlayList(playLists))
    }

    override suspend fun insertTrackToPlaylist(track: Track): Flow<Long> = flow {
        val trackInPlaylist = appDatabase.selectedTrackDao()
            .getListTrackIdOfPlaylistByPlaylistId(track.playlistId)
        if (trackInPlaylist.contains(track.trackId)) {
            emit(TRACK_INSERT)
        } else {
            val selectTrackEntity = selectTrackDbConverter.mapModelToEntity(track)
            appDatabase.selectedTrackDao().insertTrackToPlaylist(selectTrackEntity)
            updatePlaylist(track)
            emit(SUCCESS_INSERT)
        }
    }



    private suspend fun updatePlaylist(track: Track) {

        val playlistEntity = appDatabase.playListDao().getPlaylistById(track.playlistId)
        val playlist = playListConverter.mapPlayListEntityToPlayList(playlistEntity)

        val listTrackIds = appDatabase.selectedTrackDao()
            .getListTrackIdOfPlaylistByPlaylistId(track.playlistId)
        val amountTracks = listTrackIds.size
        val listTracksTime =
            appDatabase.selectedTrackDao().getListTrackTimeOfPlaylistById(track.playlistId)
        val totalTimePlaylist = listTracksTime.sum()

        playlist.listTrackIds = listTrackIds
        playlist.amountTracks = amountTracks
        playlist.trackSpelling = lastCharsTrack(amountTracks)
        playlist.totalPlaylistTime = totalTimePlaylist
        playlist.minutesSpelling = chooseSpellingMinutes(playlist.totalPlaylistTime)

        val playlistEntityNew = playListConverter.mapPlayListEntityToPlayList(playlist)
        appDatabase.playListDao().updatePlaylist(playlistEntityNew)
    }

    private fun convertPlayListEntityToPlayList(playlists: MutableList<PlayListEntity>): MutableList<PlayList> {
        return playlists.map { playlistEntity ->
            converter.mapPlayListEntityToPlayList(playlistEntity)
        }.toMutableList()
    }
    private fun convertListTracksEntityToTrack(listTracksEntity: MutableList<SelectedTrackEntity>): MutableList<Track> {
        return listTracksEntity.map { selectedTrackEntity ->
            selectTrackDbConverter.mapEntityToModel(selectedTrackEntity)
        }.toMutableList()
    }

    private fun lastCharsTrack(amountTracks: Int): String {
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
    }
}