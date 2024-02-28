package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface SearchHistoryRepository {
    fun saveHistory(tracks: List<Track>)

    fun getSearchHistory(): List<Track>
    fun clearHistory(track: MutableList<Track>)
}