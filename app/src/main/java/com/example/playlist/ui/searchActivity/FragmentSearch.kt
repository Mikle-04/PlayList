package com.example.playlist.ui.searchActivity

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.R
import com.example.playlist.databinding.SearchFragmentBinding
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.searchActivity.viewModel.TrackSearchViewModel
import com.example.playlist.ui.playActivity.PlayActivity
import com.example.playlist.ui.searchActivity.models.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent


@SuppressLint("RestrictedApi")
class FragmentSearch : Fragment(), KoinComponent {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }



    private var tracksHistory = mutableListOf<Track>()
    private var tracks = mutableListOf<Track>()
    private val adapter = AdapterTrack(tracks)
    private val adapterHistory = AdapterTrack(tracksHistory)
    private var isClickAllowed = true
    private val max = 10
    private var textWatcher: TextWatcher? = null

    private val viewModel: TrackSearchViewModel by viewModel()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksHistory.addAll(
            viewModel.getSearchHistory()
        )

        binding.recyclerViewTrack.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTrack.adapter = adapter

        binding.recyclerViewHistory.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewHistory.adapter = adapterHistory

        //visible track history
        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.layoutHistory.visibility =
                if (hasFocus && binding.editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearSearchTrack()
                    binding.layoutHistory.visibility =
                        if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
                } else {
                    binding.imgClearSearch.visibility = showClearIcon(s)
                    viewModel.searchDebounce(
                        changedText = s.toString()
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { binding.editTextSearch.addTextChangedListener(it) }

        viewModel.observeState().observe(requireActivity()) {
            render(it)
        }

        // img clean search
        binding.imgClearSearch.setOnClickListener {
            clearSearchTrack()
            val inputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
        }

        //clear history
        binding.btnHistory.setOnClickListener {
            clearHistory()
        }

        //update search
        binding.btnUpdate.setOnClickListener {
            viewModel.searchRequest(binding.editTextSearch.text.toString())
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

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory() {
        viewModel.clearSearchHistory(tracksHistory)
        adapterHistory.notifyDataSetChanged()
        binding.layoutHistory.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchTrack() {
        binding.progressBarSearch.visibility = View.GONE
        binding.imgClearSearch.visibility = View.GONE
        binding.imageViewEmpty.visibility = View.GONE
        binding.txtEmpty.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        binding.txtErrorIthernet.visibility = View.GONE
        binding.imageViewError.visibility = View.GONE
        binding.btnUpdate.visibility = View.GONE
        binding.editTextSearch.text.clear()
        tracks.clear()
        adapter.notifyDataSetChanged()
        binding.recyclerViewTrack.visibility = View.GONE
        binding.layoutHistory.visibility =
            if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.editTextSearch.removeTextChangedListener(it) }
    }


    private fun putPlayActivity(track: Track) {
        viewModel.saveSearchHistory(tracksHistory)
        Intent(requireActivity(), PlayActivity::class.java).also {
            it.putExtra("trackId", track.trackId)
            it.putExtra("track_name", track.trackName)
            it.putExtra("artist_name", track.artistName)
            it.putExtra("artwork_url", track.artworkUrl100)
            it.putExtra("time_track", track.trackTime)
            it.putExtra("collection_name", track.collectionName)
            it.putExtra("release_data", track.releaseDate)
            it.putExtra("genre_name", track.primaryGenreName)
            it.putExtra("country_name", track.country)
            it.putExtra("preview_url", track.previewUrl)
            it.putExtra("isFavourite", track.isFavourite)
            startActivity(it)
        }

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    // add track in history
    private fun addTrack(adapter: AdapterTrack, track: Track) {
        if (adapter.trackList.contains(track)) {
            adapter.trackList.removeAt(adapter.trackList.indexOf(track))
            adapter.notifyItemRemoved(adapter.trackList.indexOf(track))
            adapter.notifyItemRangeChanged(
                adapter.trackList.indexOf(track), adapter.trackList.size - 1
            )
        }
        adapter.trackList.add(0, track)
        adapter.notifyItemInserted(0)
        if (adapter.trackList.size == max + 1) {
            adapter.trackList.remove(adapter.trackList[max])
            adapter.notifyItemRemoved(10)
        }
    }


    private fun showLoading() {
        binding.progressBarSearch.visibility = View.VISIBLE
        binding.layoutHistory.visibility = View.GONE
        binding.imageViewEmpty.visibility = View.GONE
        binding.txtEmpty.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        binding.txtErrorIthernet.visibility = View.GONE
        binding.imageViewError.visibility = View.GONE
        binding.btnUpdate.visibility = View.GONE
        binding.recyclerViewTrack.visibility = View.GONE

    }

    private fun showError() {
        binding.progressBarSearch.visibility = View.GONE
        binding.imageViewEmpty.visibility = View.GONE
        binding.txtEmpty.visibility = View.GONE
        binding.txtError.visibility = View.VISIBLE
        binding.txtErrorIthernet.visibility = View.VISIBLE
        binding.imageViewError.visibility = View.VISIBLE
        binding.btnUpdate.visibility = View.VISIBLE
        binding.recyclerViewTrack.visibility = View.GONE
        binding.layoutHistory.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progressBarSearch.visibility = View.GONE
        binding.imageViewEmpty.visibility = View.VISIBLE
        binding.txtEmpty.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.txtErrorIthernet.visibility = View.GONE
        binding.imageViewError.visibility = View.GONE
        binding.btnUpdate.visibility = View.GONE
        binding.recyclerViewTrack.visibility = View.GONE
        binding.layoutHistory.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(track: List<Track>) {
        binding.progressBarSearch.visibility = View.GONE
        binding.imageViewEmpty.visibility = View.GONE
        binding.txtEmpty.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        binding.txtErrorIthernet.visibility = View.GONE
        binding.imageViewError.visibility = View.GONE
        binding.btnUpdate.visibility = View.GONE
        binding.recyclerViewTrack.visibility = View.VISIBLE
        binding.layoutHistory.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(track)
        adapter.notifyDataSetChanged()

    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading -> showLoading()
            is TrackState.Content -> showContent(state.track)
            is TrackState.Error -> showError()
            is TrackState.Empty -> showEmpty()
        }
    }
}
