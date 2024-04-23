package com.example.playlist.presentation.tracks

import com.example.playlist.domain.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TrackView : MvpView{
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: TrackState)
}