package com.example.playlist

import android.content.Context
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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class SearchActivity : AppCompatActivity() {
    private var saveText: String = ""
    private val baseUrls = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrls)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(TrackApi::class.java)
    private val tracks = ArrayList<Track>()
    private val adapter = AdapterTrack()
    private lateinit var imgEmpty: ImageView
    private lateinit var txtEmpty: TextView
    private lateinit var imgError: ImageView
    private lateinit var txtError: TextView
    private lateinit var txtErrorIthernet: TextView
    private lateinit var btnUpdate: Button

    companion object {
        private const val keySearch = "TEXT_SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)
        val imgBack = findViewById<ImageView>(R.id.img_back_search)
        val imgClearSearch = findViewById<ImageView>(R.id.img_clear_search)
        val recyclerTrack = findViewById<RecyclerView>(R.id.recyclerViewTrack)
        imgEmpty = findViewById<ImageView>(R.id.imageViewEmpty)
        txtEmpty = findViewById<TextView>(R.id.txtEmpty)
        txtError = findViewById(R.id.txtError)
        imgError = findViewById(R.id.imageViewError)
        txtErrorIthernet = findViewById(R.id.txtErrorIthernet)
        btnUpdate = findViewById(R.id.btn_update)
        if (savedInstanceState != null) editTextSearch.setText(
            savedInstanceState.getString(
                keySearch,
                ""
            )
        )

        imgBack.setOnClickListener {
            onBackPressed()
        }
        val editTextForWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    imgClearSearch.visibility = showClearIcon(p0)
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
            editTextSearch.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
        }
        adapter.trackList = tracks
        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapter
        btnUpdate.setOnClickListener {
            if (editTextSearch.text.isNotEmpty()){
                trackApiService.search(editTextSearch.text.toString())
                    .enqueue(object : Callback<TrackResponse> {
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200){
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true){
                                    hideError()
                                    tracks.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()) {
                                    showMessage()
                                }
                            } else if(response.code() != 200){
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
            if (actionId == EditorInfo.IME_ACTION_DONE){
                if (editTextSearch.text.isNotEmpty()){
                    trackApiService.search(editTextSearch.text.toString())
                        .enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(
                                call: Call<TrackResponse>,
                                response: Response<TrackResponse>
                            ) {
                                if (response.code() == 200){
                                    tracks.clear()
                                    if (response.body()?.results?.isNotEmpty() == true){
                                        hideError()
                                        tracks.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    if (response.body()?.results?.isNotEmpty() == false) {
                                        showMessage()
                                    }
                                } else if(response.code() != 200){
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

    }

    private fun showMessage(){
        imgEmpty.visibility = View.VISIBLE
        txtEmpty.visibility = View.VISIBLE
        txtError.visibility = View.GONE
        txtErrorIthernet.visibility = View.GONE
        imgError.visibility = View.GONE
        btnUpdate.visibility = View.GONE
        tracks.clear()
        adapter.notifyDataSetChanged()
    }
    private fun showError(){
        imgEmpty.visibility = View.GONE
        txtEmpty.visibility = View.GONE
        txtError.visibility = View.VISIBLE
        txtErrorIthernet.visibility = View.VISIBLE
        imgError.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
        tracks.clear()
        adapter.notifyDataSetChanged()
    }
    private fun hideError(){
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
}
//