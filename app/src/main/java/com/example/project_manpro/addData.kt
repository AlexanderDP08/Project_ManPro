package com.example.project_manpro

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.util.*

class addData : AppCompatActivity() {
    lateinit var categorySpinner : Spinner

    lateinit var _btnBack : AppCompatButton
    lateinit var _btnCancel : Button
    lateinit var _btnConfirm : Button

    //getDate
    var cal = Calendar.getInstance()
    lateinit var _btnDate: Button
    lateinit var _tvDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

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

            startActivity(Intent(this, activity_home_screen::class.java))
            finish()
        }
    }

    private fun updateDateInView() {
        val myFormat = "MMM d, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        _tvDate!!.text = sdf.format(cal.getTime())
    }
}