package com.example.playlist.ui.playActivity.models

sealed interface PlayerState {

    object  Default: PlayerState

    object Prepare: PlayerState

    object Play : PlayerState

    object Pause : PlayerState
}

