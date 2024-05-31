package com.example.playlist.ui.searchActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.creator.Creator
import com.example.playlist.R
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.viewModel.TrackSearchViewModel
import com.example.playlist.ui.playActivity.PlayActivity
import com.example.playlist.ui.searchActivity.models.TrackState


class SearchActivity : ComponentActivity() {
    private var tracksHistory = mutableListOf<Track>()
    private var tracks = mutableListOf<Track>()
    private val adapter = AdapterTrack(tracks)
    private val adapterHistory = AdapterTrack(tracksHistory)
    private lateinit var imgEmpty: ImageView
    private lateinit var txtEmpty: TextView
    private lateinit var imgError: ImageView
    private lateinit var txtError: TextView
    private lateinit var txtErrorIthernet: TextView
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var layoutProgressBar: ProgressBar
    private lateinit var btnUpdate: Button
    private lateinit var layoutHistory: LinearLayout
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var btnHistory: Button
    private lateinit var imgBack: ImageView
    private lateinit var editTextSearch: EditText
    private lateinit var imgClearSearch: ImageView
    private var isClickAllowed = true
    private lateinit var handler: Handler
    private val max = 10
    private var textWatcher: TextWatcher? = null


    private lateinit var viewModel: TrackSearchViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, TrackSearchViewModel.getModelFactory())[TrackSearchViewModel::class.java]

        editTextSearch = findViewById(R.id.edit_text_search)
        imgClearSearch = findViewById(R.id.img_clear_search)
        recyclerTrack = findViewById(R.id.recyclerViewTrack)
        imgEmpty = findViewById(R.id.imageViewEmpty)
        txtEmpty = findViewById(R.id.txtEmpty)
        txtError = findViewById(R.id.txtError)
        imgError = findViewById(R.id.imageViewError)
        txtErrorIthernet = findViewById(R.id.txtErrorIthernet)
        btnUpdate = findViewById(R.id.btn_update)
        layoutProgressBar = findViewById(R.id.progressBarSearch)
        layoutHistory = findViewById(R.id.layoutHistory)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        btnHistory = findViewById(R.id.btnHistory)
        imgBack = findViewById(R.id.img_back_search)

        Creator.setContext(this)

        tracksHistory.addAll(Creator.provideSearchHistoryRepository().getSearchHistory().toMutableList())

        handler = Handler(Looper.getMainLooper())

        recyclerTrack.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapter

        recyclerViewHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory.adapter = adapterHistory


        //visible track history
        editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            layoutHistory.visibility =
                if (hasFocus && editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    imgClearSearch.visibility = showClearIcon(s)
                    viewModel.searchDebounce(
                        changedText = s.toString()
                    )
                } else {
                    imgClearSearch.visibility = showClearIcon(s)
                    layoutHistory.visibility =
                        if (editTextSearch.hasFocus() && editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { editTextSearch.addTextChangedListener(it) }

        viewModel.observeState().observe(this){
            render(it)
        }


        // start search click keypad "enter"
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRequest(editTextSearch.text.toString())
                true
            }
            false
        }

        // img clean search
        imgClearSearch.setOnClickListener {
            clearSearchTrack()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
        }

        //clear history
        btnHistory.setOnClickListener {
            clearHistory()
        }
        imgBack.setOnClickListener {
            finish()
        }
        //update search
        btnUpdate.setOnClickListener {
            viewModel.searchRequest(editTextSearch.text.toString())
        }


        adapter.onItemClick = { track ->
            if (clickDebounce()) {
                addTrack(adapterHistory, track)

                putPlayActivity(track)
            }

        }
        adapterHistory.onItemClick = { track ->
            if (clickDebounce()) {
                putPlayActivity(track)
            }

        }
    }

    private fun showClearIcon(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearHistory() {
        Creator.provideSearchHistoryRepository().clearHistory(tracksHistory)
        adapterHistory.notifyDataSetChanged()
        layoutHistory.visibility = View.GONE
    }

    private fun clearSearchTrack() {
        layoutProgressBar.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        editTextSearch.text.clear()
        tracks.clear()
        adapter.notifyDataSetChanged()
        recyclerTrack.visibility = View.GONE
        layoutHistory.visibility =
            if (editTextSearch.hasFocus() && editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
    }


    override fun onStop() {
        super.onStop()
        Creator.provideSearchHistoryRepository().saveHistory(tracksHistory)
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { editTextSearch.removeTextChangedListener(it) }

    }

    private fun putPlayActivity(track: Track) {
        Intent(this, PlayActivity::class.java).also {
            it.putExtra("EXTRA_NAME", track.trackName)
            it.putExtra("EXTRA_AUTHOR", track.artistName)
            it.putExtra("EXTRA_IMAGE", track.artworkUrl100)
            it.putExtra("EXTRA_DURATION", track.trackTime)
            if (track.collectionName.isNotEmpty()) {
                it.putExtra("EXTRA_COLLECTION", track.collectionName)
            }
            it.putExtra("EXTRA_DATE", track.releaseDate)
            it.putExtra("EXTRA_GENRE", track.primaryGenreName)
            it.putExtra("EXTRA_COUNTRY", track.country)
            it.putExtra("EXTRA_PLAY", track.previewUrl)
            startActivity(it)

        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    // add track in history
    private fun addTrack(adapter: AdapterTrack, track: Track) {
        if (adapter.trackList.contains(track)) {
            adapter.trackList.removeAt(adapter.trackList.indexOf(track))
            adapter.notifyItemRemoved(adapter.trackList.indexOf(track))
            adapter.notifyItemRangeChanged(
                adapter.trackList.indexOf(track),
                adapter.trackList.size - 1
            )
        }
        adapter.trackList.add(0, track)
        adapter.notifyItemInserted(0)
        if (adapter.trackList.size == max + 1) {
            adapter.trackList.remove(adapter.trackList[max])
            adapter.notifyItemRemoved(10)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

  private fun showLoading() {
        layoutProgressBar.visibility = View.VISIBLE
      layoutHistory.visibility = View.GONE
    }

  private fun showError() {
        layoutProgressBar.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.VISIBLE
        txtErrorIthernet.visibility = View.VISIBLE
        imgError.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
        recyclerTrack.visibility = View.GONE
        layoutHistory.visibility = View.GONE
}

   private fun showEmpty() {
        layoutProgressBar.visibility = View.GONE
        imgEmpty.visibility = View.VISIBLE
        txtEmpty.visibility = View.VISIBLE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        layoutHistory.visibility = View.GONE
    }

   private fun showContent(track: List<Track>) {
        layoutProgressBar.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        recyclerTrack.visibility = View.VISIBLE
        layoutHistory.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(track)
        adapter.notifyDataSetChanged()

    }

    private fun render(state: TrackState) {
        when(state){
            is TrackState.Loading -> showLoading()
            is TrackState.Content -> showContent(state.track)
            is TrackState.Error -> showError()
            is TrackState.Empty -> showEmpty()
        }
    }
}
