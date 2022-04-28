package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class activity_home_screen : AppCompatActivity() {

    lateinit var _tvName : TextView
    lateinit var _btnAccPage : ImageView
    lateinit var _btnControl : AppCompatButton
    lateinit var _btnHome : AppCompatButton

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        _btnAccPage = findViewById(R.id.ivAccountPage)
        _tvName = findViewById(R.id.tvNamaUser)

        _btnAccPage.setOnClickListener {
            startActivity(Intent(this, activity_account::class.java))
        }

        db.collection("username").document(firebaseAuth.uid.toString()).get()
            .addOnSuccessListener { doc ->
                _tvName.text = doc.data!!["username"].toString()
            }
            .addOnFailureListener {

            }

        val mFragmentManager = supportFragmentManager
        val mHomeScreen = financeBalance()

        mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()

        _btnControl = findViewById(R.id.btnControl)

        _btnControl.setOnClickListener {
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = controlspending_utama()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }

        _btnHome = findViewById(R.id.btnHome)

        _btnHome.setOnClickListener {
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = financeBalance()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }
    }
}