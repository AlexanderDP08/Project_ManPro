package com.example.project_manpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Logo_Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_loading)

        Handler().postDelayed({
            val intent = Intent(this, activity_sign_in::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}