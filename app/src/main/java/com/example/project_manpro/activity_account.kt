package com.example.project_manpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class activity_account : AppCompatActivity() {

    private lateinit var fAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    lateinit var _btnLogout : Button
    lateinit var _btnBack : Button

    lateinit var _tvName : TextView
    lateinit var _tvEmail : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        fAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        _tvName = findViewById(R.id.tvAccountProfile)
        _tvEmail = findViewById(R.id.tvAccountEmail)
        _btnLogout = findViewById(R.id.btAccountLogout)
        _btnBack = findViewById(R.id.ibAccountBack)

        db.collection("username").document(fAuth.uid.toString()).get()
            .addOnSuccessListener { doc ->
                _tvName.text = doc.data!!["username"].toString()
            }
            .addOnFailureListener {
                print(it)
            }

        _tvEmail.text = fAuth.currentUser!!.email.toString()


        _btnBack.setOnClickListener {
            onBackPressed()
        }

        _btnLogout.setOnClickListener {
            fAuth.signOut()
            startActivity(Intent(this, activity_sign_in::class.java))
            finish()
        }
    }
}