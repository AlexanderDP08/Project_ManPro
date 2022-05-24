package com.example.project_manpro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [financeBalance.newInstance] factory method to
 * create an instance of this fragment.
 */
class financeBalance : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var _tvShowall : TextView
    lateinit var _btnIncome : CardView
    lateinit var _btnExpenditure : CardView

    lateinit var _tvIncome : TextView
    lateinit var _tvExpend : TextView

    lateinit var dbAuth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    var income : Int = 0
    var expend : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finance_balance, container, false)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        _tvIncome = view.findViewById(R.id.tvIncome)
        _tvExpend = view.findViewById(R.id.tvExpend)

        db.collection(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
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
                _tvIncome.setText("%,d".format(income))
                _tvExpend.setText("%,d".format(expend))
            }
            .addOnFailureListener {

            }

        //==============================
        _tvShowall = view.findViewById(R.id.tvShowall)

        _tvShowall.setOnClickListener {
            activity?.let {
                val intent = Intent(it, activity_monitor::class.java)
                it.startActivity(intent)
            }
        }

        _btnIncome = view.findViewById(R.id.cvIncome)
        _btnExpenditure = view.findViewById(R.id.cvExpenditure)

        _btnIncome.setOnClickListener {
            activity?.let {
                val intent = Intent(it, activity_monitor::class.java)
                intent.putExtra("income", true)
                it.startActivity(intent)
            }
        }
        _btnExpenditure.setOnClickListener {
            activity?.let {
                val intent = Intent(it, activity_monitor::class.java)
                intent.putExtra("expenditure", true)
                it.startActivity(intent)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment financeBalance.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            financeBalance().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}