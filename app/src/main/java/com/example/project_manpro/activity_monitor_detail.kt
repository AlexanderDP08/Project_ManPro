package com.example.project_manpro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class activity_monitor_detail : AppCompatActivity() {

    lateinit var _btnBacktoList : Button
    lateinit var _etEditTransaction : EditText
    lateinit var _etEditAmount : EditText
    lateinit var _spEditCategory : Spinner

    lateinit var _btnEditCancel : Button
    lateinit var _btnEditConfirm : Button

    //getDate
    var cal = Calendar.getInstance()
    lateinit var _btnEditDate : Button
    lateinit var _tvEditDate : TextView

    lateinit var db : FirebaseFirestore
    lateinit var authdb : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor_detail)

        db = FirebaseFirestore.getInstance()
        authdb = FirebaseAuth.getInstance()

        _btnEditConfirm = findViewById(R.id.btnEditConfirm)
        _btnEditCancel = findViewById(R.id.btnEditCancel)

        _btnBacktoList = findViewById(R.id.btnBacktoList)
        _btnBacktoList.setOnClickListener {
            onBackPressed()
        }

        _etEditTransaction = findViewById(R.id.etEditTransaction)
        _etEditAmount = findViewById(R.id.etEditAmount)

        //Category Spinner
        _spEditCategory = findViewById(R.id.spEditCategory)
        val AdapterSPCategory = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item)
        AdapterSPCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _spEditCategory.adapter = AdapterSPCategory

//        _spEditCategory = findViewById(R.id.spEditCategory)
//        val AdapterSPCategory = ArrayAdapter.createFromResource(this,
//            R.array.category, R.layout.spinner_item)
//        AdapterSPCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        _spEditCategory.adapter = AdapterSPCategory

        //getDate
        _tvEditDate = findViewById(R.id.tvEditDate)
        _btnEditDate = findViewById(R.id.btnEditDate)

        val dataIntent = intent.getParcelableExtra<dataTransaction>("dataTransaction")
        if (dataIntent != null){

            _etEditTransaction.setText(dataIntent.judul)
            _etEditAmount.setText(dataIntent.jumlah)

            _tvEditDate.setText(dataIntent.tanggal)

            val compareValue = dataIntent.kategori.toString()
            val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category,
                R.layout.spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _spEditCategory.setAdapter(adapter)
            if (compareValue != null) {
                val spinnerPosition = adapter.getPosition(compareValue)
                _spEditCategory.setSelection(spinnerPosition)
            }
        }

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
        _btnEditDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@activity_monitor_detail,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date  when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        _btnEditConfirm.setOnClickListener {
            //add data to database here
            if(_etEditTransaction.text.toString() != "" && _tvEditDate.text.toString() != "" && _etEditAmount.text.toString() != ""){
                addtoDatabase(db, _etEditTransaction.text.toString(), _tvEditDate.text.toString(), _etEditAmount.text.toString(), getCategory(_spEditCategory))
                //checkLimit()
                startActivity(Intent(this, activity_home_screen::class.java))
                finish()
            }else{
                Toast.makeText(this,"Input data belum lengkap",Toast.LENGTH_SHORT).show()
            }
        }
        _btnEditCancel.setOnClickListener {
            onBackPressed()
        }

    }

    private fun addtoDatabase(db: FirebaseFirestore, judul: String, tanggal: String, jumlah: String, kategori: String) {
        val dataIntent = intent.getParcelableExtra<dataTransaction>("dataTransaction")
            val docID = dataIntent!!.id.toString()
            val updateData = dataTransaction(
                docID,
                judul,
                tanggal,
                jumlah,
                kategori
            )
        db.collection("UserData").document("TransactionData").collection(authdb.currentUser!!.uid).document(docID).set(updateData)
        Toast.makeText(this@activity_monitor_detail, "Data Edited", Toast.LENGTH_LONG)
            .show()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        _tvEditDate.setText(sdf.format(cal.getTime()))
    }

    fun getCategory(id: Spinner): String {
        return id.selectedItem.toString()
        //Toast.makeText(this, spiner.selectedItem.toString(), Toast.LENGTH_LONG).show()
    }


}