package com.example.playlist.ui.mediaActivity.playListFragment.createPlayList

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlist.R
import com.example.playlist.databinding.FragmentCreatePlayListBinding
import com.example.playlist.ui.mediaActivity.playListFragment.createPlayList.viewModel.CreateAlbumFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlayList : Fragment() {


    private val viewModel: CreateAlbumFragmentViewModel by viewModel()

    private var _binding: FragmentCreatePlayListBinding? = null
    private val binding get() = _binding!!

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var descriptionWatcher: TextWatcher

    private var uri: Uri? = null
    private var uriImgStorage: Uri? = null
    private val currentTime by lazy { System.currentTimeMillis() }

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreatePlayListBinding.inflate(inflater, container, false)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            this.uri = uri

            if (uri == null) {
                binding.imgCover.background = null
                binding.imgCover.setImageResource(R.drawable.img_track_default)
            } else {
                setImgPlaylist(uri)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //сообщение если выходим при создании плейлиста
        binding.imgBack.setOnClickListener {
            messageExit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    messageExit()
                }
            })
        //Устанавливаем фото
        binding.imgCover.setOnClickListener {
            downloadImage()
        }

        //создать плейлист
        binding.btnCreate.setOnClickListener {
            if (binding.btnCreate.isActivated) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.create_playlist, binding.nameEditText.text),
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.createPlaylist(
                binding.nameEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                saveImgPlayList()
            )

            requireActivity().supportFragmentManager.popBackStack()
        }

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.nameEditText.isActivated = false
                    binding.hintName.isVisible= false
                    binding.btnCreate.isActivated = false

                } else {
                    binding.nameEditText.isActivated = true
                    binding.hintName.isVisible = true
                    binding.btnCreate.isActivated = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.nameEditText.addTextChangedListener(nameTextWatcher)

        descriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.descriptionEditText.isActivated = false
                    binding.hintDescription.isVisible= false

                } else {
                    binding.descriptionEditText.isActivated = true
                    binding.hintDescription.isVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.descriptionEditText.addTextChangedListener(descriptionWatcher)

    }

    fun saveImgPlayList(): Uri? {
        if (uri != null) {

            val imgCover = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_cover"
            )

            if (!imgCover.exists()) {
                imgCover.mkdir()
            }
            val imageFile = File(imgCover, "$currentTime.jpg")
            val inputStream = requireContext().contentResolver.openInputStream(uri!!)
            val outputStream = FileOutputStream(imageFile)
            uriImgStorage = Uri.fromFile(imageFile)

            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            return uriImgStorage
        } else {
            return null
        }
    }

    private fun downloadImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun messageExit() {
        if (uri == null && binding.nameEditText.text.isNullOrEmpty() && binding.descriptionEditText.text.isNullOrEmpty()) {
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            showExitMsg()
        }
    }

    private fun showExitMsg() {
        MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistDialogStyle).setTitle(
            getString(R.string.close_create_playlist)
        )
            .setMessage(getString(R.string.save_msg))
            .setPositiveButton(getString(R.string.complete)) { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                //
            }.show()
    }

    //Устанавливаем фото плей листа
    fun setImgPlaylist(uri: Uri) {
        binding.imgCover.apply {
            setImageURI(uri)
            background =
                ContextCompat.getDrawable(context, R.drawable.img_playlist_background)
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}