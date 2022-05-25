package com.example.project_manpro

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        db.collection("limit").document(fAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                myLimit.text = doc.data!!["limit"].toString()
            }
            .addOnFailureListener {
                print(it)
            }

        db.collection("reminder").document(fAuth.currentUser!!.uid).get()
            .addOnSuccessListener { doc ->
                myReminder.text = doc.data!!["reminder"].toString()
            }
            .addOnFailureListener {
                print(it)
            }

        _btnEdit=view.findViewById(R.id.btnEdit)
        _btnEdit.setOnClickListener {
            val fragmentEdit = control_spending_1()
            val fragmentM = parentFragmentManager
            fragmentM.beginTransaction().apply {
                replace(R.id.fragmentContainer, fragmentEdit, control_spending_1::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
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