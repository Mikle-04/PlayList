package com.example.playlist.data.playList

import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.data.playList.converter.PlayListConverter
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.playList.api.PlayListRepository

class PlayListRepositoryImpl(private val appDatabase: AppDatabase, private val converter: PlayListConverter): PlayListRepository {
    override suspend fun insertPlayList(playList: PlayList) {
        val playListEntity = converter.mapPlayListEntityToPlayList(playList)
        appDatabase.playListDao().insertPlaylist(playListEntity)
    }
}