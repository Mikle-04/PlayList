package com.example.playlist.ui.searchActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.Creator
import com.example.playlist.R
import com.example.playlist.domain.models.Track
import com.example.playlist.data.network.TrackApi
import com.example.playlist.domain.api.TrackInteractor
import com.example.playlist.ui.PlayActivity
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val tracks = mutableListOf<Track>()
    private var tracksHistory = mutableListOf<Track>()
    private val adapter = AdapterTrack(tracks)
    private val adapterHistory = AdapterTrack(tracksHistory)
    private lateinit var imgEmpty: ImageView
    private lateinit var txtEmpty: TextView
    private lateinit var imgError: ImageView
    private lateinit var txtError: TextView
    private lateinit var txtErrorIthernet: TextView
    private lateinit var btnUpdate: Button
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var txtHistory: TextView
    private lateinit var btnHistory: Button
    private lateinit var layoutHistory: LinearLayout
    private lateinit var layoutProgressBar: ProgressBar
    private lateinit var recyclerTrack: RecyclerView
    private val searchRunable = Runnable { searchRequest() }
    lateinit var editTextSearch: EditText
    private var isClickAllowed = true
    lateinit var handler: Handler
    private val max = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        editTextSearch = findViewById<EditText>(R.id.edit_text_search)
        val imgBack = findViewById<ImageView>(R.id.img_back_search)
        val imgClearSearch = findViewById<ImageView>(R.id.img_clear_search)
        recyclerTrack = findViewById<RecyclerView>(R.id.recyclerViewTrack)
        imgEmpty = findViewById<ImageView>(R.id.imageViewEmpty)
        txtEmpty = findViewById<TextView>(R.id.txtEmpty)
        txtError = findViewById(R.id.txtError)
        imgError = findViewById(R.id.imageViewError)
        txtErrorIthernet = findViewById(R.id.txtErrorIthernet)
        btnUpdate = findViewById(R.id.btn_update)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        txtHistory = findViewById(R.id.txtHistorySearch)
        btnHistory = findViewById(R.id.btnHistory)
        layoutHistory = findViewById(R.id.layoutHistory)
        layoutProgressBar = findViewById(R.id.progressBarSearch)
        handler = Handler(Looper.getMainLooper())
        Creator.setContext(this)

        tracksHistory = Creator.provideSearchHistoryRepository().getSearchHistory().toMutableList()



        imgBack.setOnClickListener {
            onBackPressed()
        }


        btnHistory.setOnClickListener {
            Creator.provideSearchHistoryRepository().clearHistory(tracksHistory)
            adapterHistory.notifyDataSetChanged()
            layoutHistory.visibility = View.GONE
        }

        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            layoutHistory.visibility =
                if (hasFocus && editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val editTextForWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    imgClearSearch.visibility = showClearIcon(p0)
                    searchDebounce()
                } else {
                    imgClearSearch.visibility = showClearIcon(p0)
                    layoutHistory.visibility =
                        if (editTextSearch.hasFocus() && p0?.isEmpty() == true && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        editTextSearch.addTextChangedListener(editTextForWatcher)



        imgClearSearch.setOnClickListener {
            editTextSearch.text.clear()
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
        }
        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = AdapterTrack(tracks)
        recyclerViewHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory.adapter = AdapterTrack(tracksHistory)

        adapter.onItemClick = { track ->
            if (clickDebounce()) {
                addTrack(adapterHistory, track)

                callPlayActivity(track)
            }

        }
        adapterHistory.onItemClick = { track ->
            if (clickDebounce()) {
                callPlayActivity(track)
            }

        }



        btnUpdate.setOnClickListener {
            searchRequest()
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest()
                true
            }
            false
        }

    }

    fun searchRequest() {
        if (editTextSearch.text.isNotEmpty()) {
            handler.removeCallbacksAndMessages(RunnableTag)
            hideError()
            layoutProgressBar.visibility = View.VISIBLE
            Creator.provideTrackInteractor().searchTrack(
                editTextSearch.text.toString(),
                object : TrackInteractor.TrackConsumer {
                    override fun consume(foundMovies: List<Track>?) {
                        if (foundMovies == null) {
                            handler.post { showError() }
                        } else {
                            if (foundMovies.isEmpty()) {
                                handler.post { showMessage() }
                            } else {
                                handler.post { render(foundMovies) }
                            }
                        }
                    }

                })
        }
    }

    private fun render(foundMovies: List<Track>) {
        tracks.clear()
        tracks.addAll(foundMovies)
        adapter.notifyDataSetChanged()
        layoutProgressBar.visibility = View.GONE
        recyclerTrack.visibility = View.VISIBLE
    }


    override fun onStop() {
        super.onStop()
        Creator.provideSearchHistoryRepository().saveHistory(tracksHistory)
        handler.removeCallbacks(searchRunable)
    }

    private fun showMessage() {
        layoutProgressBar.visibility = View.GONE
        imgEmpty.visibility = View.VISIBLE
        txtEmpty.visibility = View.VISIBLE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
    }

    private fun showError() {
        layoutProgressBar.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.VISIBLE
        txtErrorIthernet.visibility = View.VISIBLE
        imgError.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
    }

    private fun hideError() {
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
    }

    private fun showClearIcon(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun callPlayActivity(track: Track) {
        val intent = Intent(this, PlayActivity::class.java).also {
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

    private fun searchDebounce() {
        handler.removeCallbacksAndMessages(RunnableTag)
        handler.postDelayed(searchRunable, RunnableTag, SEARCH_DEBOUNCE_DELAY)
    }

    fun addTrack(adapter: AdapterTrack, track: Track) {
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
        private const val RunnableTag = "SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
