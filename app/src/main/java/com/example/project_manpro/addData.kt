package com.example.project_manpro

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class addData : AppCompatActivity() {
    lateinit var categorySpinner : Spinner

    lateinit var _btnBack : AppCompatButton
    lateinit var _btnCancel : Button
    lateinit var _btnConfirm : Button

    lateinit var _etTransaction : EditText
    lateinit var _etAmount : EditText

    //getDate
    var cal = Calendar.getInstance()
    lateinit var _btnDate: Button
    lateinit var _tvDate: TextView

    lateinit var db : FirebaseFirestore
    lateinit var authdb : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        db = FirebaseFirestore.getInstance()
        authdb = FirebaseAuth.getInstance()

        _etTransaction = findViewById(R.id.etTransaction)
        _etAmount = findViewById(R.id.etAmount)

        _btnBack = findViewById(R.id.btnBacktoHome)
        _btnBack.setOnClickListener {
            onBackPressed()
        }

        //getDate
        _tvDate = findViewById(R.id.tvDate)
        _btnDate = findViewById(R.id.btnDate)
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        _btnDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@addData,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        //Category Spinner
        categorySpinner = findViewById(R.id.categorySpinner)
        val AdapterSPCategory = ArrayAdapter.createFromResource(this,
            R.array.category, R.layout.spinner_item)
        AdapterSPCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = AdapterSPCategory



        _btnCancel = findViewById(R.id.btnCancel)
        _btnConfirm = findViewById(R.id.btnConfirm)
        _btnCancel.setOnClickListener {
            onBackPressed()
        }
        _btnConfirm.setOnClickListener {
            //add data to database here
            addtoDatabase(db, _etTransaction.text.toString(), _tvDate.text.toString(), _etAmount.text.toString(), getCategory(categorySpinner))
            startActivity(Intent(this, activity_home_screen::class.java))
            finish()
        }
    }

    private fun addtoDatabase(db: FirebaseFirestore, judul: String, tanggal: String, jumlah: String, kategory: String) {
        val data = dataTransaction(judul, tanggal, jumlah, kategory)
        db.collection(authdb.currentUser!!.uid)
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this@addData, "Data added", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@addData,"Data Gagal disimpan",Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
    }

    private fun updateDateInView() {
        val myFormat = "MMM d, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        _tvDate!!.text = sdf.format(cal.getTime())
    }

    fun getCategory(id: Spinner): String {
        return id.selectedItem.toString()
        //Toast.makeText(this, spiner.selectedItem.toString(), Toast.LENGTH_LONG).show()
    }
}