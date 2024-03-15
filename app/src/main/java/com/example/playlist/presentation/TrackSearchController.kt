package com.example.playlist.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.Creator
import com.example.playlist.R
import com.example.playlist.domain.api.TrackInteractor
import com.example.playlist.domain.models.Track
import com.example.playlist.ui.PlayActivity
import com.example.playlist.ui.searchActivity.AdapterTrack
import com.example.playlist.ui.searchActivity.SearchActivity

class TrackSearchController(private val activity: Activity, private val adapter: AdapterTrack) {
}