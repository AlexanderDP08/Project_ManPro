package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class activity_sign_up : AppCompatActivity() {

    lateinit var _edName : EditText
    lateinit var _edEmail : EditText
    lateinit var _edPassword : EditText
    lateinit var _btnRegister : Button

    private lateinit var progressDialog : ProgressDialog
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        _edName = findViewById(R.id.etSignUpName)
        _edEmail = findViewById(R.id.etSignUpEmail)
        _edPassword = findViewById(R.id.etSignUpPassword)
        _btnRegister = findViewById(R.id.btnSignUpRegister)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()


        _btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (TextUtils.isEmpty(_edName.text.toString().trim())){
            _edName.error = "Please enter name"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(_edEmail.text.toString().trim()).matches()){
            _edEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(_edPassword.text.toString().trim())){
            _edPassword.error = "Please enter password"
        }
        else if (_edPassword.text.toString().trim().length < 6){
            _edPassword.error = "Password must atleast 6 character long"
        }
        else {
            firebaseSignIn()
        }
    }

    private fun firebaseSignIn() {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(_edEmail.text.toString().trim(), _edPassword.text.toString().trim())
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                //Add Username to Username Database with Firebase Firestore
                var addData = uid(_edName.text.toString().trim())
                var addLimit = climit("0")
                var addReminder = creminder("50")
                db = FirebaseFirestore.getInstance()

//                val docID = genDocID()
//                var addData = uid(_edName.text.toString().trim(), docID)
//                db.collection("UserData").document("Username").collection(firebaseUser.uid).document(docID).set(addData)
                db.collection("username").document(firebaseUser.uid).set(addData)
                db.collection("limit").document(firebaseUser.uid).set(addLimit)
                db.collection("reminder").document(firebaseUser.uid).set(addReminder)

                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Account created with email ${email}",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this, activity_home_screen::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Sign In failed due to ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun genDocID() : String{
        val myChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random(System.nanoTime())
        val id = StringBuilder()

        for (i in 0 until 20){
            val rIndex = random.nextInt(myChar.length)
            id.append(myChar[rIndex])
        }

        return id.toString()
    }
}