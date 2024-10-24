package com.example.playlist.ui.mediaActivity.playListFragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlist.R
import com.example.playlist.domain.playList.models.PlayList
import com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.CreatePlayListFragment
import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.EditorPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class FragmentEditor : CreatePlayListFragment() {

    override val viewModel: EditorPlayListViewModel by viewModel {
        parametersOf(playlistId)
    }

    val playlistId: Int by lazy { requireArguments().getInt(PLAYLIST_ID) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderPlaylistEditScreen()

        binding.imgBack.setOnClickListener {
            exitFromPlaylistEditFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitFromPlaylistEditFragment()
                }
            })

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlist ->

            setPlaylistData(playlist)
            var newUriCoverImageStorage = playlist.imgStorage
            var isImageCoverChanged = false

            binding.imgCover.setOnClickListener {
                isImageCoverChanged = true
                downloadImage()
            }

            binding.btnCreate.setOnClickListener {

                if (binding.btnCreate.isActivated) {

                    if (isImageCoverChanged) newUriCoverImageStorage = saveImgPlayList()

                    viewModel.saveUpdatePlayList(
                        playlist,
                        binding.nameEditText.text.toString(),
                        binding.descriptionEditText.text.toString(),
                        newUriCoverImageStorage
                    )

                    exitFromPlaylistEditFragment()
                }
            }
        }

    }

    private fun renderPlaylistEditScreen() {
        binding.apply {
            newPlaylistTextView.text = getString(R.string.edit_text)
            btnCreate.text = getString(R.string.save)
        }
    }

    private fun setPlaylistData(playlist: PlayList) {
        binding.apply {
            nameEditText.setText(playlist.namePlaylist)
            descriptionEditText.setText(playlist.descriptionPlaylist)
            if (playlist.imgStorage.toString() != "null") setImgPlaylist(playlist.imgStorage!!)
        }
    }

    private fun exitFromPlaylistEditFragment() {
        findNavController().navigateUp()
    }

    companion object {
        const val PLAYLIST_ID = "playlistId"
        fun createArgs(playlistId: Int): Bundle = bundleOf(PLAYLIST_ID to playlistId)
    }

}