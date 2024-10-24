package com.example.playlist.ui.mediaActivity.favoriteFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist.databinding.FragmentFavouritesBinding
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.favoriteFragment.state.HistoryState
import com.example.playlist.ui.mediaActivity.favoriteFragment.viewModel.FavouriteFragmentViewModel
import com.example.playlist.ui.playActivity.PlayActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouritesFragment : Fragment(){

    companion object{
        fun newInstance() = FavouritesFragment()
        private const val CLICK_DEBOUNCE = 1000L
        private const val KEY_TRACK = "track"
    }

    private val viewModel  by viewModel<FavouriteFragmentViewModel>()

    private var _binding :FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var adapter: FavouriteAdapter? = null

    private lateinit var favouriteList: RecyclerView
    private lateinit var imgEmptyList : ImageView
    private lateinit var textEmptyList : TextView

    private var isClicked = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavouriteAdapter()

        favouriteList = binding.recyclerViewFavourite
        imgEmptyList = binding.favouriteEmptyImg
        textEmptyList = binding.mediatekaEmptyText

        favouriteList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favouriteList.adapter = adapter



        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        adapter?.onItemClick = {track ->
            if (clickDebounce()) {
                putPlayActivity(track)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
        favouriteList.adapter = null

    }

    override fun onStart() {
        super.onStart()
        viewModel.getFavouriteTrack()
    }

    private fun render(state: HistoryState){
        when(state){
            is HistoryState.Content -> showContent(state.track)
            is HistoryState.Empty -> showEmpty()
            else -> {}
        }
    }

    private fun showEmpty(){
        favouriteList.visibility = View.GONE
        imgEmptyList.visibility = View.VISIBLE
        textEmptyList.visibility = View.VISIBLE
    }

    private fun showContent(track: List<Track>){
        favouriteList.visibility = View.VISIBLE
        imgEmptyList.visibility = View.GONE
        textEmptyList.visibility = View.GONE

        adapter?.favouriteTrack?.clear()
        adapter?.favouriteTrack?.addAll(track)
        adapter?.notifyDataSetChanged()
    }

    private fun putPlayActivity(track: Track) {
        val intent = Intent(context, PlayActivity::class.java)
        intent.putExtra(KEY_TRACK, track)
        startActivity(intent)

    }

    private fun clickDebounce(): Boolean {
        val current = isClicked
        if (isClicked) {
            isClicked= false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE)
                isClicked = true
            }
        }
        return current
    }
}