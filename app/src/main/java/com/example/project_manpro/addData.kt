package com.example.project_manpro

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    lateinit var locale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        db = FirebaseFirestore.getInstance()
        authdb = FirebaseAuth.getInstance()
        locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)

        _etTransaction = findViewById(R.id.etEditTransaction)
        _etAmount = findViewById(R.id.etEditAmount)

        _btnBack = findViewById(R.id.btnBacktoList)
        _btnBack.setOnClickListener {
            onBackPressed()
        }

        //getDate
        _tvDate = findViewById(R.id.tvDate)
        _btnDate = findViewById(R.id.btnEditDate)
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
        categorySpinner = findViewById(R.id.spEditCategory)
        val AdapterSPCategory = ArrayAdapter.createFromResource(this,
            R.array.category, R.layout.spinner_item)
        AdapterSPCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = AdapterSPCategory



        _btnCancel = findViewById(R.id.btnEditCancel)
        _btnConfirm = findViewById(R.id.btnEditConfirm)
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
        val docID = genDocID()
        val data = dataTransaction(docID, judul, tanggal, jumlah, kategory)
        db.collection("UserData").document("TransactionData").collection(authdb.currentUser!!.uid).document(docID)
            .set(data)
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
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        _tvDate!!.text = sdf.format(cal.getTime())
    }

    fun getCategory(id: Spinner): String {
        return id.selectedItem.toString()
        //Toast.makeText(this, spiner.selectedItem.toString(), Toast.LENGTH_LONG).show()
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