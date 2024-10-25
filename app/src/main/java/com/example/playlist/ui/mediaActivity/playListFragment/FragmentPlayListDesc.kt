package com.example.playlist.ui.mediaActivity.playListFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist.R
import com.example.playlist.databinding.FragmentPlayListBinding
import com.example.playlist.databinding.FragmentPlayListDescBinding
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.domain.search.models.Track
import com.example.playlist.ui.mediaActivity.playListFragment.state.TrackPlayListState
import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.PlayListDescViewModel
import com.example.playlist.ui.playActivity.PlayActivity
import com.example.playlist.ui.playActivity.PlayActivity.Companion.KEY_TRACK
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.milliseconds

class FragmentPlayListDesc : Fragment() {


    private var _binding: FragmentPlayListDescBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlayListDescViewModel>()

    private val playlistId: Int by lazy { requireArguments().getInt(ARG_PLAYLIST) }

    private val onClickTrack = object : TrackPlayListAdapter.OnClickTrackListener {
        override fun onClickTrack(track: Track) {
            startPlayer(track)

        }

        override fun onLongClickTrack(track: Track) {
            MaterialAlertDialogBuilder(requireContext(), R.style.PlayListDialogStyle)
                .setTitle(getString(R.string.delete_track))
                .setMessage(getString(R.string.do_you_want_to_delete_track))
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    //
                }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                    viewModel.deleteSelectedTrackFromPlaylist(track)
                }.show()
        }

    }

    private var adapterTrackPlaylist = TrackPlayListAdapter(onClickTrack)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListDescBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = binding.tracksBottomSheet



        viewModel.getPlaylist(playlistId)
        viewModel.getTracksByPlaylistId(playlistId)

        binding.trackRecyclerView.adapter = adapterTrackPlaylist

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

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

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.menuButton.setOnClickListener {
            binding.apply {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.textDeletePlaylist.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.PlayListDialogStyle
            ).setMessage(getString(R.string.do_you_wont_to_delete_playlist))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    //
                }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylistById(playlistId)
                }.show()
        }

        binding.textEditInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragmentPlayListDesc_to_fragmentEditor,
                FragmentEditor.createArgs(playlistId)
            )
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlist ->
            renderTracksBottomSheet(playlist)
            renderMenuBottomSheet(playlist)
        }

        viewModel.getListTracksOfPlaylistState()
            .observe(viewLifecycleOwner) { tracksOfPlaylistState ->
                when (tracksOfPlaylistState) {

                    is TrackPlayListState.ContentPlaylist -> {

                        val reverseListTracksOfPlaylist = tracksOfPlaylistState.listTracksPlaylist.reversed()

                        adapterTrackPlaylist.listTracksOfPlaylist = reverseListTracksOfPlaylist as MutableList<Track>
                        adapterTrackPlaylist.notifyDataSetChanged()

                        binding.buttonShare.setOnClickListener {
                            viewModel.shareLinkPlaylist(playlistId)
                        }

                        binding.textShare.setOnClickListener {
                            viewModel.shareLinkPlaylist(playlistId)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }

                    is TrackPlayListState.EmptyPlaylist -> {

                        binding.apply {
                            trackRecyclerView.isVisible = false
                            textMessage.isVisible = true
                        }


                        binding.buttonShare.setOnClickListener {
                            messageEmptyPlaylist()
                        }

                        binding.textShare.setOnClickListener {
                            messageEmptyPlaylist()
                        }
                    }
                }
            }

        viewModel.getStateDelete().observe(viewLifecycleOwner) { deleteState ->
            if (deleteState.isComplete) {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startPlayer(track: Track) {
        val playerIntent = Intent(
            context, PlayActivity::class.java
        )
        playerIntent.putExtra(
            KEY_TRACK, track
        )
        startActivity(playerIntent)
    }

    private fun messageEmptyPlaylist() {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.playlist_not_have_tracks),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun renderTracksBottomSheet(playlist: PlayList) {
        binding.apply {
            namePlaylist.text = playlist.namePlaylist
            if (playlist.descriptionPlaylist.isNullOrEmpty()) {
                descriptionPlaylist.isVisible = false
            } else {
                descriptionPlaylist.isVisible = true
                descriptionPlaylist.text = playlist.descriptionPlaylist
            }
            coverPlaylist.setImageURI(playlist.imgStorage)
            totalTime.text =
                "${playlist.totalPlaylistTime.milliseconds.inWholeMinutes} ${playlist.minutesSpelling}"
            amountTracks.text = "${playlist.amountTracks} ${playlist.trackSpelling}"
        }
    }

    private fun renderMenuBottomSheet(playlist: PlayList) {
        binding.apply {
            if (playlist.imgStorage.toString() != getString(R.string.null_value)) {
                coverPlaylistLinear.setImageURI(playlist.imgStorage)
            }
            namePlaylistLinear.text = playlist.namePlaylist
            amountTracksLinear.text = playlist.amountTracks.toString()
            titleTrackLinear.text = playlist.trackSpelling
        }
    }
    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    companion object {
        const val ARG_PLAYLIST = "playlist"
        fun createArgs(playlistID: Int): Bundle = bundleOf(ARG_PLAYLIST to playlistID)
    }


}