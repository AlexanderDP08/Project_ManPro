package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class activity_home_screen : AppCompatActivity() {

    lateinit var _tvName : TextView
    lateinit var _btnAccPage : ImageView

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
        /*_tvEmail = findViewById(R.id.tvHomeScreenEmail)
        _tvUID = findViewById(R.id.tvHomeScreenUID)

        _btnLogout = findViewById(R.id.btnHomeScreenLogout)

        firebaseAuth = FirebaseAuth.getInstance()

        _tvEmail.text = firebaseAuth.currentUser!!.email
        _tvUID.text = firebaseAuth.currentUser!!.uid

        _btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, activity_sign_in::class.java))
            finish()
        }
        checkUser()*/
    }

    /*private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            _tvEmail.text = firebaseUser!!.email
            _tvUID.text = firebaseUser!!.uid
        }
        else {
            startActivity(Intent(this, activity_sign_in::class.java))
        }
    }*/
}