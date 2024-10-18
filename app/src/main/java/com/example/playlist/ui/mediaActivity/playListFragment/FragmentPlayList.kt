package com.example.playlist.ui.mediaActivity.playListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist.databinding.FragmentPlayListBinding
import com.example.playlist.ui.mediaActivity.playListFragment.state.PlayListState
import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlayList : Fragment() {

    companion object {
        fun newInstance() = FragmentPlayList()

    }

    private val viewModel: PlayListViewModel by viewModel()

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private val adapter = PlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerPlaylist.adapter = adapter

        binding.btnNewPlayList.setOnClickListener {
            findNavController().navigate(com.example.playlist.R.id.action_fragmentMedia_to_createPlayList)
        }

        viewModel.getStatePlayList().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayListState.Content -> {
                    binding.imgEmptyList.isVisible = false
                    binding.txtEmptyList.isVisible = false
                    adapter.listPlayList = state.playlists
                    adapter.notifyDataSetChanged()
                }

                is PlayListState.Empty -> {
                    binding.imgEmptyList.isVisible = true
                    binding.txtEmptyList.isVisible = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlayListDb()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}