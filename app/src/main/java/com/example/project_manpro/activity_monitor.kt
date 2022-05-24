package com.example.project_manpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class activity_monitor : AppCompatActivity() {
    lateinit var _btnMonitorBack : Button
    lateinit var _rvItem : RecyclerView

    private var arData = arrayListOf<dataTransaction>()

    val adapter = adapterAllData(arData)

    lateinit var db : FirebaseFirestore
    lateinit var dbAuth : FirebaseAuth

    var income : Boolean = false
    var expenditure : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        db = FirebaseFirestore.getInstance()
        dbAuth = FirebaseAuth.getInstance()

        _rvItem = findViewById(R.id.rvItem)

        _btnMonitorBack = findViewById(R.id.btnMonitorBack)
        _btnMonitorBack.setOnClickListener {
            onBackPressed()
        }
        income = intent.getBooleanExtra("income", false)
        expenditure = intent.getBooleanExtra("expenditure", false)


        readData(db)
        viewData()
    }

    private fun readData(db: FirebaseFirestore) {
        db.collection(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                arData.clear()
                for (document in it){
                    val cat = document.data.get("kategori").toString()
                    val newData = dataTransaction(
                        document.data.get("judul").toString(),
                        document.data.get("tanggal").toString(),
                        document.data.get("jumlah").toString(),
                        document.data.get("kategori").toString()
                    )
                    if (income){
                        if(cat == "Account Receivable" ||
                            cat == "Additional Income"||
                            cat == "Bonus"||
                            cat == "Allowance"||
                            cat == "Capital Gain"||
                            cat == "Income"||
                            cat == "Refund"||
                            cat == "Salary"||
                            cat == "Savings"||
                            cat == "Business Profit"){
                            arData.add(newData)
                        }
                    }
                    else if (expenditure){
                        if(cat != "Account Receivable" &&
                            cat != "Additional Income"&&
                            cat != "Bonus"&&
                            cat != "Allowance"&&
                            cat != "Capital Gain"&&
                            cat != "Income"&&
                            cat != "Refund"&&
                            cat != "Salary"&&
                            cat != "Savings"&&
                            cat != "Business Profit"){
                            arData.add(newData)
                        }
                    }
                    else {
                        arData.add(newData)
                    }

                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this@activity_monitor,"Data failed to load", Toast.LENGTH_SHORT).show()
            }

    }

    private fun viewData() {
        _rvItem.layoutManager = LinearLayoutManager(this)
        //_rvData.setHasFixedSize(true)
        //adapter = adapterHistory(arData)
        _rvItem.adapter = adapter
    }
}