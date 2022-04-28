package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class activity_sign_in : AppCompatActivity() {

    lateinit var _etEmail : EditText
    lateinit var _etPassword : EditText
    lateinit var _tvCreateNewAccount : TextView
    lateinit var _btnSubmit : Button
    lateinit var  _tvForgotPass : TextView

    private lateinit var progressDialog : ProgressDialog
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        _etEmail = findViewById(R.id.etSignInEmail)
        _etPassword = findViewById(R.id.etSignInPassword)

        _btnSubmit = findViewById(R.id.btnSignInSubmit)
        _tvCreateNewAccount = findViewById(R.id.tvSignInCreateNew)
        _tvForgotPass = findViewById(R.id.tvSignInForgotPass)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        checkUser()

        _tvCreateNewAccount.setOnClickListener{
            startActivity(Intent(this, activity_sign_up::class.java))
        }

        _btnSubmit.setOnClickListener {
            validateData()
        }

        _tvForgotPass.setOnClickListener {
            startActivity(Intent(this, activity_reset_password::class.java))
        }
    }

    private fun validateData() {
        if(!Patterns.EMAIL_ADDRESS.matcher(_etEmail.text.toString().trim()).matches()){
            _etEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(_etPassword.text.toString().trim())){
            _etPassword.error = "Please enter password"
        }
        else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(_etEmail.text.toString().trim(), _etPassword.text.toString().trim())
            .addOnSuccessListener {
                progressDialog.dismiss()

                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(
                    this,
                    "Logged In as ${email}",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, activity_home_screen::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Login failed due to ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun checkUser() {
        val firebaseUser =  firebaseAuth.currentUser
        if (firebaseUser != null){
            startActivity(Intent(this, activity_home_screen::class.java))
            finish()
        }
    }
}