package com.example.playlist.ui.playActivity.models

import com.example.playlist.R

sealed class FavouriteState() {

    class Default : FavouriteState()

    class Favourite : FavouriteState()
}