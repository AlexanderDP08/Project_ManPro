package com.example.project_manpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class add_baru : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_baru)

        var btn = findViewById<Button>(R.id.arrow_left_)
        btn.setOnClickListener {  }
    }
}