package com.example.playlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        imgShare.setOnClickListener {
            val message = "https://practicum.yandex.ru/android-developer/"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(intent)
        }

        imgMode.setOnClickListener {
            val email = arrayOf("Godmordor@gmail.com")
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение"
            val modeIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }

            startActivity(Intent.createChooser(modeIntent, null))
        }

        imgAgreementNext.setOnClickListener {
            val uri = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val agreementIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(agreementIntent)
        }


    }
}