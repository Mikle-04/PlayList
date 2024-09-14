package com.example.playlist.ui.searchActivity.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.data.favourite.db.AppDatabase
import com.example.playlist.domain.search.api.SearchHistoryRepository
import com.example.playlist.domain.search.api.TrackInteractor
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.models.TrackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TrackSearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val repositoryHistory: SearchHistoryRepository,
    private val appDatabase: AppDatabase
) : ViewModel(), KoinComponent {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    var historyTrack : List<Track>? = null
    private var lastSearchText: String? = null

    //create liveData
    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }


    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        lastSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(lastSearchText.toString())
        }

    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(TrackState.Loading)

            viewModelScope.launch {
                trackInteractor
                    .searchTrack(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }

            }

        }
    }

    private fun processResult(listTrack: List<Track>?, errorMessage: String?) {

        val track = mutableListOf<Track>()

        if (listTrack != null) {
            track.addAll(listTrack)
        }

        when {
            errorMessage != null -> {
                renderState(
                    TrackState.Error(errorMessage)
                )

            }

            track.isEmpty() -> {
                renderState(
                    TrackState.Empty(
                        message = ""
                    )
                )
            }

            else -> {
                renderState(
                    TrackState.Content(
                        track = track
                    )
                )
            }
        }

    }


    fun getSearchHistory(): Flow<List<Track>> = flow{
        val historyList = repositoryHistory.getSearchHistory()
        val favoritesIdList = appDatabase.trackDao().getTrackId()
        if (favoritesIdList.isNotEmpty()){
            historyList.forEach{
                it.isFavourite = isFavourite(it.trackId, favoritesIdList)
            }
        }
        emit(historyList)
}

fun saveSearchHistory(track: List<Track>) {
    repositoryHistory.saveHistory(track)
}

fun clearSearchHistory(track: MutableList<Track>) {
    repositoryHistory.clearHistory(track)
}

private fun isFavourite(trackId: Int, favoritesIdList: List<Int>): Boolean {
    val favorite = favoritesIdList.find { it == trackId }
    return favorite != null
}

}