package com.example.project_manpro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class activity_home_screen : AppCompatActivity() {

    lateinit var _tvName : TextView
    lateinit var _btnAccPage : ImageView
    lateinit var _btnControlOff : AppCompatButton
    lateinit var _btnControlOn : AppCompatButton
    lateinit var _btnHomeOff : AppCompatButton
    lateinit var _btnHomeOn : AppCompatButton
    lateinit var _btnAddData : AppCompatButton
    lateinit var _tvBalance : TextView
    lateinit var _btnHideAmount : ImageButton
    var expend = 0.0

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var progressDialog : ProgressDialog

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 1000

    //time
    var currentSec = 0
    var counter = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        currentSec = getCurrentSec().toInt()
        currentSec = (60 - currentSec) - 2

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCanceledOnTouchOutside(false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        _btnAccPage = findViewById(R.id.ivAccountPage)
        _tvName = findViewById(R.id.tvNamaUser)

        _btnAccPage.setOnClickListener {
            startActivity(Intent(this, activity_account::class.java))
        }

        db.collection("username").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                _tvName.setText(it.data!!.get("username").toString())
            }

        val mFragmentManager = supportFragmentManager
        val mHomeScreen = financeBalance()
        mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()

        _btnControlOff = findViewById(R.id.btnControlOff)
        _btnControlOn = findViewById(R.id.btnControlOn)
        _btnControlOn.visibility = View.INVISIBLE
        _btnHomeOff = findViewById(R.id.btnHomeOff)
        _btnHomeOn = findViewById(R.id.btnHomeOn)
        _btnHomeOn.visibility = View.VISIBLE

        _btnControlOff.setOnClickListener {
            _btnControlOn.visibility = View.VISIBLE
            _btnHomeOn.visibility = View.INVISIBLE
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = controlspending_utama()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }
        _btnHomeOff.setOnClickListener {
            _btnControlOn.visibility = View.INVISIBLE
            _btnHomeOn.visibility = View.VISIBLE
            val mFragmentManager = supportFragmentManager
            val mHomeScreen = financeBalance()

            mFragmentManager.findFragmentByTag(activity_home_screen::class.java.simpleName)
            mFragmentManager.beginTransaction().add(R.id.fragmentContainer, mHomeScreen, activity_home_screen::class.java.simpleName).commit()
        }

        _btnAddData = findViewById(R.id.btnAddData)
        _btnAddData.setOnClickListener {
            startActivity(Intent(this, addData::class.java))
        }

        _tvBalance = findViewById(R.id.tvBalance)
        getBalance()

        _btnHideAmount = findViewById(R.id.hideAmount)
        _btnHideAmount.setOnClickListener {
            if(_tvBalance.text == "Rp. ****"){
                getBalance()
            }else{
                _tvBalance.setTextColor(Color.WHITE)
                _tvBalance.setText("Rp. ****")
            }
        }
        getCurrentExpend()
        checkNotif()
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            time()
            //Toast.makeText(this@PlayScreen, "This method will run every 10 seconds", Toast.LENGTH_SHORT).show()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    fun getCurrentSec(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("ss")
        val time = now.format(formatter)
        return time
    }

    private fun time(){
        if (currentSec > 0){
            currentSec -= 1
            if(currentSec == 0){
                checkNotif()
                currentSec = 5
            }
        }
    }

    private fun checkNotif(){
        var limit = 0.0
        var reminder : Double = 0.0
        var persen : Double = 0.0
        var limitformated = ""
        db.collection("limit").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                limit = doc.data!!["limit"].toString().toDouble()
                db.collection("reminder").document(firebaseAuth.currentUser!!.uid).get()
                    .addOnSuccessListener { doc ->
                        progressDialog.dismiss()
                        reminder = doc.data!!["reminder"].toString().toDouble()
                        persen = (expend/limit) * 100

                        limitformated = "Rp. " + "%,d".format(limit.toInt())

                        Log.e("Hasil LimitFormated: ", limitformated)
                        Log.e("Hasil Persen: ", persen.toString())
                        Log.e("Hasil Expend: ", expend.toString())
                        Log.e("Hasil Limit: ", limit.toString())
                        Log.e("Hasil Reminder: ", reminder.toString())
                        if(reminder.toInt() <= persen.toInt()){
                                Log.e("IF Reminder: ", reminder.toString())
                                Log.e("IF Persen: ", persen.toString())
                            showNotification("Control Spending Reminder!",
                                "You have used ${String.format("%.1f", persen).toDouble().toString()}% of your current spending limit of ${limitformated.toString()}"
                            )
                        }
                        else {

                        }
                    }
            }

    }

    fun getCurrentExpend() {
        db.collection("UserData").document("TransactionData").collection(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val cat = document.data.get("kategori").toString()
                    val tgl = document.data.get("tanggal").toString()
                    val bulan = tgl.subSequence(5, 7)
                    //Log.e("bln: ", bulan.toString())
                    val sdf = SimpleDateFormat("yyyy/MM/dd")
                    val currentDate = sdf.format(Date()).toString()
                    val curDateMonth = currentDate.subSequence(5, 7)
                    //Log.e("curmonth: ", curDateMonth.toString())
                    if(bulan == curDateMonth){
                        if (cat != "Account Receivable" &&
                            cat != "Additional Income" &&
                            cat != "Bonus" &&
                            cat != "Allowance" &&
                            cat != "Capital Gain" &&
                            cat != "Income" &&
                            cat != "Refund" &&
                            cat != "Salary" &&
                            cat != "Savings" &&
                            cat != "Business Profit"
                        ) {
                            expend += document.data.get("jumlah").toString().toInt()
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Error: ", it.toString())
            }
    }

    fun showNotification(title: String, message: String) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("1",
                "channel_1",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "this_is_channel_1"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "1")
            .setSmallIcon(R.drawable.logo_my_money) // notification icon
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))
            .setAutoCancel(true) // clear notification after click
        val intent = Intent(applicationContext, activity_home_screen::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }

    private fun getBalance (){
        progressDialog.show()
        var income = 0
        var expend = 0
        db.collection("UserData").document("TransactionData").collection(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                progressDialog.dismiss()
                for (document in it) {
                    val cat = document.data.get("kategori").toString()
                    if (cat == "Account Receivable" ||
                        cat == "Additional Income" ||
                        cat == "Bonus" ||
                        cat == "Allowance" ||
                        cat == "Capital Gain" ||
                        cat == "Income" ||
                        cat == "Refund" ||
                        cat == "Salary" ||
                        cat == "Savings" ||
                        cat == "Business Profit"){
                        income += document.data.get("jumlah").toString().toInt()
                    }

                    if (cat != "Account Receivable" &&
                        cat != "Additional Income" &&
                        cat != "Bonus" &&
                        cat != "Allowance" &&
                        cat != "Capital Gain" &&
                        cat != "Income" &&
                        cat != "Refund" &&
                        cat != "Salary" &&
                        cat != "Savings" &&
                        cat != "Business Profit"){
                        expend += document.data.get("jumlah").toString().toInt()
                    }
                }
                _tvBalance.setText("Rp. " + "%,d".format(income-expend))
                if((income-expend) < 0){
                    _tvBalance.setTextColor(Color.RED)
                }else if ((income-expend) >= 0){
                    _tvBalance.setTextColor(Color.WHITE)
                }else{

                }

            }
            .addOnFailureListener {

            }
    }
}