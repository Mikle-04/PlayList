package com.example.playlist.ui.mediaActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlist.R
import com.example.playlist.databinding.ActivityMainBinding
import com.example.playlist.databinding.ActivityMediaBinding
import com.example.playlist.ui.mediaActivity.adapter.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMediaBinding

    private lateinit var tabMediator : TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerMedia.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.imgMediaBack.setOnClickListener {
            finish()
        }

        tabMediator = TabLayoutMediator(binding.tabLayoutMedia, binding.viewPagerMedia){tab, position ->
            when(position){
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }

        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}