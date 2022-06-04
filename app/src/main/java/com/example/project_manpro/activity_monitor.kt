package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.exp

class activity_monitor : AppCompatActivity() {
    lateinit var _btnMonitorBack : Button
    lateinit var _rvItem : RecyclerView

    private var arData = arrayListOf<dataTransaction>()

    val adapter = adapterAllData(arData)

    lateinit var db : FirebaseFirestore
    lateinit var dbAuth : FirebaseAuth

    lateinit var _btnIncome : Button
    lateinit var _btnExpend : Button

    var income : Boolean = false
    var expenditure : Boolean = false

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCanceledOnTouchOutside(false)

        db = FirebaseFirestore.getInstance()
        dbAuth = FirebaseAuth.getInstance()

        _rvItem = findViewById(R.id.rvItemFB)

        _btnIncome = findViewById(R.id.btnIncomeME)
        _btnExpend = findViewById(R.id.btnExpendME)

        _btnIncome.setOnClickListener {
            if(income){
                income = false
                expenditure = false
                readData(db)
            }
            else {
                income = true
                expenditure = false
                readData(db)
            }
        }
        _btnExpend.setOnClickListener {
            if(expenditure){
                income = false
                expenditure = false
                readData(db)
            }
            else {
                income = false
                expenditure = true
                readData(db)
            }

        }

        _btnMonitorBack = findViewById(R.id.btnMonitorBack)
        _btnMonitorBack.setOnClickListener {
            onBackPressed()
        }
        income = intent.getBooleanExtra("income", false)
        expenditure = intent.getBooleanExtra("expenditure", false)


        readData(db)
        printData()
    }

    private fun readData(db: FirebaseFirestore) {
        progressDialog.show()
        db.collection("UserData").document("TransactionData").collection(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                progressDialog.dismiss()
                arData.clear()
                for (document in it){
                    val cat = document.data.get("kategori").toString()
                    val newData = dataTransaction(
                        document.data.get("id").toString(),
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
                arData.sortByDescending { it.tanggal }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this@activity_monitor,"Data failed to load", Toast.LENGTH_SHORT).show()
            }

    }

    private fun printData() {
        _rvItem.layoutManager = LinearLayoutManager(this)
        //_rvData.setHasFixedSize(true)
        //adapter = adapterHistory(arData)
        _rvItem.adapter = adapter
        adapter.setOnItemClickCallback(object : adapterAllData.OnItemClickCallback {
            override fun onItemClicked(data: dataTransaction) {
                val intent = Intent(this@activity_monitor, activity_monitor_detail::class.java)
                intent.putExtra("dataTransaction",data)
                startActivity(intent)
            }
        })
    }
}