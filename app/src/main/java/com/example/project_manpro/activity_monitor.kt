package com.example.project_manpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class activity_monitor : AppCompatActivity() {
    lateinit var _btnMonitorBack : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        _btnMonitorBack = findViewById(R.id.btnMonitorBack)
        _btnMonitorBack.setOnClickListener {
            onBackPressed()
        }
    }
}