package com.example.playlist.data.search.dto

import com.example.playlist.domain.search.models.Track

class TrackResponse(val resultCount : Int, val results : List<TrackDto>): Response()