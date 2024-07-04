package com.example.playlist.ui.mediaActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist.databinding.FragmentFavouritesBinding
import com.example.playlist.databinding.FragmentPlayListBinding
import com.example.playlist.ui.mediaActivity.viewModel.PlayListFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object{
        fun newInstance() = PlayListFragment()
    }

    private val viewModel : PlayListFragmentViewModel by viewModel()

    private var _binding : FragmentPlayListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}