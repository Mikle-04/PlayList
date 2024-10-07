package com.example.playlist.ui.mediaActivity.playListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.R
import com.example.playlist.databinding.FragmentPlayListBinding
import com.example.playlist.ui.mediaActivity.playListFragment.viewModel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlayList : Fragment() {

    companion object{
        fun newInstance() = FragmentPlayList()
    }

    private val viewModel: PlayListViewModel by viewModel()

    private var _binding : FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewPlayList.setOnClickListener {
            findNavController().navigate(com.example.playlist.R.id.action_fragmentPlayList_to_createPlayList)
        }
    }
}