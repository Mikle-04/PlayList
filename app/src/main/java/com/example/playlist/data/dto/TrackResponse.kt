package com.example.playlist.data.dto

import com.example.playlist.domain.models.Track

class TrackResponse(val resultCount : Int, val results : List<TrackDto>):Response()