package com.example.project_manpro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_control_spending_2.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_control_spending_2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    lateinit var _btnCancel: AppCompatButton
    lateinit var _btnConfirm: AppCompatButton
    lateinit var _newReminder: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        _newReminder = view.findViewById(R.id.cs2edNewReminder)

        val fragmentMainControl = controlspending_utama()
        val fragmentM = parentFragmentManager
        _btnCancel=view.findViewById(R.id.cs2cancel_control)
        _btnConfirm=view.findViewById(R.id.cs2confirm_control)

        _btnCancel.setOnClickListener {
            fragmentM.beginTransaction().apply {
                replace(R.id.fragmentContainer, fragmentMainControl, controlspending_utama::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        _btnConfirm.setOnClickListener {
            if(_newReminder.text.toString() != "" && _newReminder.text.toString().toInt() >= 10 && _newReminder.text.toString().toInt() <= 90){
                val addReminder = creminder(_newReminder.text.toString())
                db.collection("reminder").document(fAuth.currentUser!!.uid).set(addReminder)
                fragmentM.beginTransaction().apply {
                    replace(R.id.fragmentContainer, fragmentMainControl, controlspending_utama::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }else{
                Toast.makeText(activity,"Input data hanya bisa range 10-90",Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_spending_2, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment control_spending_1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            control_spending_1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}