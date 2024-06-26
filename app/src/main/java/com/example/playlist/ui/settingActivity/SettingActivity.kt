package com.example.playlist.ui.settingActivity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlist.R
import com.example.playlist.ui.settingActivity.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    lateinit var switchCompat: SwitchCompat
    lateinit var imgShare: ImageView
    lateinit var imgMode: ImageView
    lateinit var imgAgreementNext: ImageView
    lateinit var imgBack: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
      //  Creator.setContext(this)


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