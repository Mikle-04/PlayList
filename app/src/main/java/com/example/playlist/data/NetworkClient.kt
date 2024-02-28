package com.example.playlist.data

import com.example.playlist.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}