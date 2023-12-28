package com.example.playlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var saveText: String = ""
    private val baseUrls = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrls)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(TrackApi::class.java)
    private val tracks = mutableListOf<Track>()
    private var tracksHistory = mutableListOf<Track>()
    private lateinit var adapter: AdapterTrack
    private lateinit var adapterHistory: AdapterTrack
    private lateinit var imgEmpty: ImageView
    private lateinit var txtEmpty: TextView
    private lateinit var imgError: ImageView
    private lateinit var txtError: TextView
    private lateinit var txtErrorIthernet: TextView
    private lateinit var btnUpdate: Button
    private lateinit var historyGroup: LinearLayout
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var searchHistory: SearchHistory
    private lateinit var txtHistory: TextView
    private lateinit var btnHistory: Button
    private lateinit var layoutHistory: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)
        val imgBack = findViewById<ImageView>(R.id.img_back_search)
        val imgClearSearch = findViewById<ImageView>(R.id.img_clear_search)
        val recyclerTrack = findViewById<RecyclerView>(R.id.recyclerViewTrack)
        searchHistory = SearchHistory(this)
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

        tracksHistory = searchHistory.getSearchHistory().toMutableList()

        if (savedInstanceState != null) editTextSearch.setText(
            savedInstanceState.getString(
                keySearch,
                ""
            )
        )
        imgBack.setOnClickListener {
            onBackPressed()
        }
        btnHistory.setOnClickListener {
            searchHistory.clearHistory(tracksHistory)
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
                layoutHistory.visibility =
                    if (editTextSearch.hasFocus() && p0?.isEmpty() == true && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
                recyclerTrack.visibility = showClearIcon(p0)
                if (!p0.isNullOrEmpty()) {
                    imgClearSearch.visibility = showClearIcon(p0)
                    layoutHistory.visibility =
                        if (editTextSearch.hasFocus() && p0.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
                    recyclerTrack.visibility = showClearIcon(p0)
                } else {
                    imgClearSearch.visibility = showClearIcon(p0)


                }
            }

            override fun afterTextChanged(p0: Editable?) {
                saveText = p0.toString()


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
        adapter = AdapterTrack(tracks)
        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterHistory = AdapterTrack(tracksHistory)
        recyclerViewHistory.adapter = adapterHistory
        adapter.onItemClick = {track ->
            searchHistory.addTrack(adapterHistory, track)
            callPlayActivity(track)
        }
        adapterHistory.onItemClick = {track ->
            callPlayActivity(track)
        }



        btnUpdate.setOnClickListener {
            if (editTextSearch.text.isNotEmpty()) {
                trackApiService.search(editTextSearch.text.toString())
                    .enqueue(object : Callback<TrackResponse> {
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    hideError()
                                    tracks.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()) {
                                    showMessage()
                                }
                            } else if (response.code() != 200) {
                                showError()
                            }

                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            showError()
                        }

                    })
            }
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editTextSearch.text.isNotEmpty()) {
                    trackApiService.search(editTextSearch.text.toString())
                        .enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(
                                call: Call<TrackResponse>,
                                response: Response<TrackResponse>
                            ) {
                                if (response.code() == 200) {
                                    tracks.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        hideError()
                                        tracks.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    if (response.body()?.results?.isNotEmpty() == false) {
                                        showMessage()
                                    }
                                } else if (response.code() != 200) {
                                    showError()
                                }

                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                showError()
                            }

                        })
                }
                true
            }
            false
        }

        onStop()

    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveHistory(tracksHistory)
    }

    private fun showMessage() {
        imgEmpty.visibility = View.VISIBLE
        txtEmpty.visibility = View.VISIBLE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showError() {
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.VISIBLE
        txtErrorIthernet.visibility = View.VISIBLE
        imgError.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    private fun hideError() {
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(keySearch, saveText)
    }

    private fun showClearIcon(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        private const val keySearch = "TEXT_SEARCH"
    }

    fun callPlayActivity(track: Track){
        val intent = Intent(this, PlayActivity::class.java).also {
            it.putExtra("EXTRA_NAME", track.trackName)
            it.putExtra("EXTRA_AUTHOR", track.artistName)
            it.putExtra("EXTRA_IMAGE", track.artworkUrl100)
            it.putExtra("EXTRA_DURATION", track.trackTime)
            if (track.collectionName.isNotEmpty()){
                it.putExtra("EXTRA_COLLECTION", track.collectionName)
            }
            it.putExtra("EXTRA_DATE", track.releaseDate)
            it.putExtra("EXTRA_GENRE", track.primaryGenreName)
            it.putExtra("EXTRA_COUNTRY", track.country)
            startActivity(it)

        }
    }


}