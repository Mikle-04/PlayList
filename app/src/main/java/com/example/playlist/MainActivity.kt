package com.example.playlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnMedia = findViewById<Button>(R.id.btn_media)
        val btnSettings = findViewById<Button>(R.id.btn_setting)

        val clickSearch: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Search", Toast.LENGTH_SHORT).show()
            }
        }

        btnSearch.setOnClickListener(clickSearch)
        btnMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Media", Toast.LENGTH_SHORT).show()
        }
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

    }
}