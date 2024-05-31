package com.example.playlist.domain.search.api

import com.example.playlist.domain.search.models.Track

interface SearchHistoryRepository {
    fun saveHistory(tracks: List<Track>)
    fun getSearchHistory(): List<Track>
    fun clearHistory(track: MutableList<Track>)
}