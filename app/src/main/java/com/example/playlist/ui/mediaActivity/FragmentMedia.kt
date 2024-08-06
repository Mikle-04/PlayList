package com.example.playlist.ui.mediaActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist.R
import com.example.playlist.databinding.MediaFragmentBinding
import com.example.playlist.ui.mediaActivity.adapter.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMedia : Fragment() {

    private var _binding : MediaFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator : TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MediaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerMedia.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayoutMedia, binding.viewPagerMedia){tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.favourites_track)
                1 -> tab.text = resources.getString(R.string.play_list)
            }

        }

        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }
}