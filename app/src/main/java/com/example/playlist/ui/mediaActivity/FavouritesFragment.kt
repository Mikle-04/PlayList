package com.example.playlist.ui.mediaActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist.databinding.FragmentFavouritesBinding
import com.example.playlist.ui.mediaActivity.viewModel.FavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouritesFragment : Fragment(){

    companion object{
        fun newInstance() = FavouritesFragment()
    }

    private val viewModel : FavoriteFragmentViewModel by viewModel()

    private var _binding :FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}