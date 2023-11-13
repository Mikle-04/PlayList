package com.example.playlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val imgShare = findViewById<ImageView>(R.id.img_share)
        val imgMode = findViewById<ImageView>(R.id.img_mode)
        val imgAgreementNext = findViewById<ImageView>(R.id.img_agreement_next)
        val imgBack = findViewById<ImageView>(R.id.img_back)
        val switchCompat = findViewById<SwitchCompat>(R.id.switch_theme)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        switchCompat.setOnCheckedChangeListener{_, checkedId ->
            when(checkedId){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        imgShare.setOnClickListener {
            val message = getString(R.string.link_message)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(intent)
        }

        imgMode.setOnClickListener {
            val email = arrayOf(getString(R.string.email_message))
            val subject = getString(R.string.subject_message)
            val message = getString(R.string.dev_message)
            val modeIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }

            startActivity(Intent.createChooser(modeIntent, null))
        }

        imgAgreementNext.setOnClickListener {
            val uri = Uri.parse(getString(R.string.uri_message))
            val agreementIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(agreementIntent)
        }


    }
}