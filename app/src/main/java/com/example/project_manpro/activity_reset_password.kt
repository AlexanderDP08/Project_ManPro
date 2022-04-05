package com.example.project_manpro

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class activity_reset_password : AppCompatActivity() {

    lateinit var _etEmail : EditText
    lateinit var _btnSendEmail : Button

    private lateinit var progressDialog : ProgressDialog
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        _etEmail = findViewById(R.id.edResetPasswordEmail)
        _btnSendEmail = findViewById(R.id.btnResetPasswordSendEmail)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Sending email verification...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        _btnSendEmail.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if(!Patterns.EMAIL_ADDRESS.matcher(_etEmail.text.toString().trim()).matches()){
            _etEmail.error = "Invalid email format"
        }
        else {
            firebaseSendEmail()
        }
    }

    private fun firebaseSendEmail() {
        progressDialog.show()
        firebaseAuth.sendPasswordResetEmail(_etEmail.text.toString().trim())
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Email sent to ${_etEmail.text}.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(
                this,
                "Email failed to send due to ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}