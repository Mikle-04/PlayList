package com.example.playlist.ui.playActivity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist.R
import com.example.playlist.databinding.ActivityPlayBinding
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.CreatePlayListFragment
import com.example.playlist.ui.mediaActivity.playListFragment.state.PlayListState
import com.example.playlist.ui.playActivity.models.PlayerState
import com.example.playlist.ui.playActivity.state.InsertTrackPlayListState
import com.example.playlist.ui.playActivity.viewModel.PlayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayActivity : AppCompatActivity() {

    companion object{
        private const val KEY_TRACK = "track"
    }

    private lateinit var track: Track

    private val viewModel: PlayViewModel by viewModel() {
        parametersOf(track)
    }

    private lateinit var binding: ActivityPlayBinding


    private var uri_img: String = ""

    private val adapterList = PlayerListAdapter { playList ->
        viewModel.insertTrackToPlayList(track, playList )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet)

        getIntentSearchActivity()
        getCoverArtwork()


        binding.recyclePlayList.adapter = adapterList

        binding.backImg.setOnClickListener {
            this.onBackPressed()
        }


        binding.playImg.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observeState().observe(this) {
            renderState(it)
            binding.playImg.isEnabled = it.isPlayButtonEnabled
            binding.playImg.setImageResource(it.imgPlay)
            binding.playSecondTxt.text = it.progress

        }



        binding.likeImg.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }
        viewModel.observeStateFavourite().observe(this) { favourite ->
            if (favourite != null)
                favouriteState(favourite)
        }

        binding.addInPlayList.setOnClickListener {
            viewModel.getPlayListDb()
            binding.apply {
                overlay.isVisible = true
                playlistBottomSheet.isVisible = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.btnNewPlaylist.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view_player, CreatePlayListFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }

        })

        supportFragmentManager.addOnBackStackChangedListener {
            viewModel.getPlayListDb()
        }

        viewModel.getStatePlaylist().observe(this){statePlayList ->
        when(statePlayList){
            is PlayListState.Content -> {
                binding.scrollViewRecycler.isVisible = true
                adapterList.playlists = statePlayList.playlists
                adapterList.notifyDataSetChanged()
            }

            is PlayListState.Empty -> {
                binding.scrollViewRecycler.isVisible = false
            }
        }

        }

        viewModel.getStateInsertTrack().observe(this){insertTrackState ->
            when(insertTrackState){
                is InsertTrackPlayListState.Fail ->{
                    Toast.makeText(
                        this,
                        getString(R.string.track_is_in_playlist) + insertTrackState.namePlaylist,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is InsertTrackPlayListState.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.add_to_the_playlist) + insertTrackState.namePlaylist,
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                else -> {}
            }
        }


    }

    private fun getIntentSearchActivity() {
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KEY_TRACK, Track::class.java)!!
        } else {
            intent.getParcelableExtra(KEY_TRACK)!!
        }

        binding.apply {
            nameTrackTxt.text = track.trackName
            authorTxt.text = track.artistName
            timeTxt.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            albumNameTxt.text = track.collectionName
            yearTxt.text = track.releaseDate.take(4)
            genreTxt.text = track.primaryGenreName
            countryTxt.text = track.country
        }
        uri_img = track.artworkUrl100.toString()

        binding.albumNameTxt.isSelected = true

    }

    private fun getCoverArtwork() {
        Glide.with(applicationContext)
            .load(uri_img.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(
                CenterCrop(),
                RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.size_8dp))
            )
            .placeholder(R.drawable.img_track_default)
            .into(binding.coverImg)
    }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun renderState(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Playing -> {
                binding.playImg.setImageResource(R.drawable.stop_button)
            }

            is PlayerState.Prepared, is PlayerState.Paused, is PlayerState.Default -> {
                binding.playImg.setImageResource(R.drawable.play_button)

            }

        }

    }

    private fun favouriteState(isFavourite: Boolean) {
        if (isFavourite) {
            binding.likeImg.setImageResource(R.drawable.like_click_button)
        } else {
            binding.likeImg.setImageResource(R.drawable.like_button)
        }

    }


}
//