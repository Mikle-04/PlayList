package com.example.playlist.ui.playActivity.models

import com.example.playlist.R

sealed class FavouriteState(val isFavourite: Boolean) {

    class Default : FavouriteState(false)

    class Favourite : FavouriteState(true)
}