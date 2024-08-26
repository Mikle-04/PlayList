package com.example.playlist.ui.playActivity.models

import com.example.playlist.R

sealed class FavouriteState(val isFavourite: Boolean, val imgFavourite : Int) {

    class Default : FavouriteState(false, R.drawable.like_button)

    class Favourite : FavouriteState(true, R.drawable.like_click_button)
}