package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.google.common.base.CharMatcher.invisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class activity_home_screen : AppCompatActivity() {

    lateinit var _tvName : TextView
    lateinit var _btnAccPage : ImageView
    lateinit var _btnControlOff : AppCompatButton
    lateinit var _btnControlOn : AppCompatButton
    lateinit var _btnHomeOff : AppCompatButton
    lateinit var _btnHomeOn : AppCompatButton
    lateinit var _btnAddData : AppCompatButton

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

        _btnControlOff = findViewById(R.id.btnControlOff)
        _btnControlOn = findViewById(R.id.btnControlOn)
        _btnControlOn.visibility = View.INVISIBLE
        _btnHomeOff = findViewById(R.id.btnHomeOff)
        _btnHomeOn = findViewById(R.id.btnHomeOn)
        _btnHomeOn.visibility = View.VISIBLE

        _btnControlOff.setOnClickListener {
            _btnControlOn.visibility = View.VISIBLE
            _btnHomeOn.visibility = View.INVISIBLE
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = controlspending_utama()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }

        _btnHomeOff.setOnClickListener {
            _btnControlOn.visibility = View.INVISIBLE
            _btnHomeOn.visibility = View.VISIBLE
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = financeBalance()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }


        _btnAddData = findViewById(R.id.btnAddData)

        _btnAddData.setOnClickListener {
            startActivity(Intent(this, addData::class.java))
        }

    }
}