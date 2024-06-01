package com.example.playlist.ui.settingActivity

import android.content.res.Configuration
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlist.domain.settings.App
import com.example.playlist.R
import com.example.playlist.creator.Creator
import com.example.playlist.ui.settingActivity.viewModel.SettingsViewModel


class SettingActivity : AppCompatActivity() {
    lateinit var switchCompat: SwitchCompat
    lateinit var viewModel: SettingsViewModel
    lateinit var imgShare: ImageView
    lateinit var imgMode: ImageView
    lateinit var imgAgreementNext: ImageView
    lateinit var imgBack: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        Creator.setContext(this)
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        imgShare = findViewById<ImageView>(R.id.img_share)
        imgMode = findViewById<ImageView>(R.id.img_mode)
        imgAgreementNext = findViewById<ImageView>(R.id.img_agreement_next)
        imgBack = findViewById<ImageView>(R.id.img_back)
        switchCompat = findViewById<SwitchCompat>(R.id.switch_theme)

        viewModel.observeTheme.observe(this) {
            switchTheme(it)
        }

        switchCompat.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }


        imgBack.setOnClickListener {
            finish()
        }

        imgShare.setOnClickListener {
            viewModel.shareLink()
        }

        imgMode.setOnClickListener {
            viewModel.openSupport()
        }

        imgAgreementNext.setOnClickListener {
            viewModel.openTerm()
        }


    }

    private fun switchTheme(theme: Boolean) {
        switchCompat.isChecked = theme
    }
}