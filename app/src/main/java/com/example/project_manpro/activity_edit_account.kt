package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.FirestoreGrpc

class activity_edit_account : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var authdb : FirebaseAuth

    lateinit var _edUsername : EditText
    lateinit var _tvConfirmER : TextView
    lateinit var _btnBacktoProfile : Button

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCanceledOnTouchOutside(false)

        db = FirebaseFirestore.getInstance()
        authdb = FirebaseAuth.getInstance()

        _edUsername = findViewById(R.id.edUsername)
        _tvConfirmER = findViewById(R.id.tvConfirmER)
        _btnBacktoProfile = findViewById(R.id.btnBacktoProfile)

        _btnBacktoProfile.setOnClickListener {
            onBackPressed()
        }

        setUsername(db, authdb)
        _tvConfirmER.setOnClickListener {
            val newName = _edUsername.text.toString()
            if(newName.isNotEmpty()){
                if(newName.length > 16){
                    Toast.makeText(
                        this,
                        "Username can not be more than 16",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                else {
                    editUsername(db, authdb, newName)
                }
            }
            else {
                Toast.makeText(
                    this,
                    "Username can not be Empty",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

    }

    private fun setUsername(db: FirebaseFirestore, authdb: FirebaseAuth){
        val uid = authdb.currentUser!!.uid
        progressDialog.show()
        db.collection("username").document(uid).get()
            .addOnSuccessListener {
                progressDialog.dismiss()
                _edUsername.setText(it.data!!.get("username").toString())
            }
            .addOnFailureListener {

            }
    }

    private fun editUsername(db: FirebaseFirestore, authdb: FirebaseAuth, newUsername: String){
        val uid = authdb.currentUser!!.uid
        val newID = uid(newUsername)
        progressDialog.show()
        db.collection("username").document(uid).set(newID)
        progressDialog.dismiss()
        startActivity(Intent(this, activity_account::class.java))
        finish()
    }
}