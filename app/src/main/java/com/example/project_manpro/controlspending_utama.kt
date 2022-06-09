package com.example.project_manpro

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [controlspending_utama.newInstance] factory method to
 * create an instance of this fragment.
 */
class controlspending_utama : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    lateinit var _btnEdit : AppCompatButton
    lateinit var _btnReminder : AppCompatButton

    var limit : Double = 0.0
    var spending : Double = 0.0
    var reminder : Double = 0.0
    var expend : Int = 0

    lateinit var _tv1 : TextView
    lateinit var _tv2 : ImageView
    lateinit var _tv3: ImageView
    lateinit var _tv4 : ImageView
    lateinit var _tvSpendingLimit: TextView

    private lateinit var progressDialog : ProgressDialog
    lateinit var _imBear : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myLimit = view.findViewById<TextView>(R.id.textView13)
        val myReminder = view.findViewById<TextView>(R.id.textView11)
        fAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCanceledOnTouchOutside(false)

        _tv1 = view.findViewById(R.id.tvSpending1)
        _tv2 = view.findViewById(R.id.tvSpending2)
        _tv3 = view.findViewById(R.id.tvSpending3)
        _tv4 = view.findViewById(R.id.tvSpending4)
        _imBear = view.findViewById(R.id.imBear)
        _tvSpendingLimit = view.findViewById(R.id.tvSpendingLimit)

        db.collection("UserData").document("TransactionData").collection(fAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val cat = document.data.get("kategori").toString()
                    val tgl = document.data.get("tanggal").toString()
                    val bulan = tgl.subSequence(5, 7)
                    Log.e("bln: ", bulan.toString())
                    val sdf = SimpleDateFormat("yyyy/MM/dd")
                    val currentDate = sdf.format(Date()).toString()
                    val curDateMonth = currentDate.subSequence(5, 7)
                    Log.e("curmonth: ", curDateMonth.toString())
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
                myReminder.text = "Rp. " + "%,d".format(expend)
            }
            .addOnFailureListener {

            }

        progressDialog.show()
        db.collection("limit").document(fAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                limit = doc.data!!["limit"].toString().toDouble()
                myLimit.text = "Rp. " + "%,d".format(limit.toInt())
                db.collection("reminder").document(fAuth.currentUser!!.uid).get()
                    .addOnSuccessListener { doc ->
                        progressDialog.dismiss()
                        spending = doc.data!!["reminder"].toString().toDouble()
                        _tvSpendingLimit.setText("Your Spending Limit: $spending%")
                        //myReminder.text = spending.toString()

                        var persen = (expend/limit)*100
                        _tv1.text = "$persen%"

                        setSpendingBackground(persen)

                        Log.e("Hasil Persen: ", persen.toString())
                        Log.e("Hasil Limit: ", limit.toString())
                        Log.e("Hasil Spending: ", spending.toString())
                    }
                    .addOnFailureListener {
                        print(it)
                    }

            }
            .addOnFailureListener {
                print(it)
            }

        _btnEdit=view.findViewById(R.id.btnEdit)
        db.collection("limit").document(fAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                "Rp. " + "%,d".format(doc.data!!["limit"].toString().toInt())
            }
            .addOnFailureListener {
                print(it)
            }

        db.collection("reminder").document(fAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                reminder = doc.data!!["reminder"].toString().toDouble()
            }
            .addOnFailureListener {
                print(it)
            }

        _btnEdit=view.findViewById(R.id.btnEdit)
        _btnReminder=view.findViewById(R.id.btnEditReminder)
        _btnEdit.setOnClickListener {
            val fragmentEdit = control_spending_1()
            val fragmentM = parentFragmentManager
            Log.e(tag, myLimit.text.toString())
            fragmentM.beginTransaction().apply {
                replace(R.id.fragmentContainer, fragmentEdit, control_spending_1::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        _btnReminder.setOnClickListener {
            val fragmentEdit = fragment_control_spending_2()
            val fragmentM = parentFragmentManager
            Log.e(tag, myLimit.text.toString())
            fragmentM.beginTransaction().apply {
                replace(R.id.fragmentContainer, fragmentEdit, fragment_control_spending_2::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setSpendingBackground(persen : Double) {
        if(persen >= reminder){
            _tv1.setTextColor(getColor(requireContext(), R.color.white))
            _imBear.setBackgroundResource(R.drawable.bear_shock)
            _tv1.setBackgroundResource(R.drawable.frame_button_tambah)
            _tv2.setBackgroundResource(R.drawable.frame_button_tambah)
            _tv3.setBackgroundResource(R.drawable.frame_button_tambah)
            _tv4.setBackgroundResource(R.drawable.frame_button_tambah)
        }
        else{
            _imBear.setBackgroundResource(R.drawable.bear_jempol)
            _tv1.setBackgroundResource(R.drawable.frame_your_spending)
            _tv2.setBackgroundResource(R.drawable.frame_your_spending)
            _tv3.setBackgroundResource(R.drawable.frame_your_spending)
            _tv4.setBackgroundResource(R.drawable.frame_your_spending)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_controlspending_utama, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment controlspending_utama.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            controlspending_utama().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}