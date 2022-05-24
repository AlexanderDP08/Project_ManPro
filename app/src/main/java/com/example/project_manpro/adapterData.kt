package com.example.project_manpro

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Time
import java.text.DecimalFormat
import java.text.NumberFormat

data class uid (
    val username: String? = null
)

data class dataTransaction (
    val judul: String? = null,
    val tanggal: String? = null,
    val jumlah: String? = null,
    val kategori: String? = null
)

class adapterAllData (private val listItem: ArrayList<dataTransaction>) :
        RecyclerView.Adapter<adapterAllData.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val _circle = itemView.findViewById<ImageView>(R.id.ivCircle)
        val _logo = itemView.findViewById<ImageView>(R.id.ivLogo)

        val _judul = itemView.findViewById<TextView>(R.id.tvTransaction)
        val _tanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        val _jumlah = itemView.findViewById<TextView>(R.id.tvAmount)
        val _kategori = itemView.findViewById<TextView>(R.id.tvCategory)

        val _min = itemView.findViewById<TextView>(R.id.tvMin)
        val _plus = itemView.findViewById<TextView>(R.id.tvPlus)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterAllData.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_all_transaction_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterAllData.ListViewHolder, position: Int) {
        val itemDetails = listItem[position]

        holder._plus.visibility = View.INVISIBLE
        holder._min.visibility = View.INVISIBLE

        holder._judul.setText(itemDetails.judul)
        holder._tanggal.setText(itemDetails.tanggal)

        holder._kategori.setText(itemDetails.kategori)
        val cat = itemDetails.kategori

        //Account receivable, Additional Income, Bonus, Allowance,
        //Capital Gain, Cash withdrawal, Cost & taxes, Income, Insurance, Investment,
        //Debt, Refund, Salary, Savings, Business Profit
        if(cat == "Account Receivable" ||
            cat == "Additional Income"||
            cat == "Bonus"||
            cat == "Allowance"||
            cat == "Capital Gain"||
            cat == "Cash Withdrawal"||
            cat == "Cost and Taxes"||
            cat == "Income"||
            cat == "Insurance"||
            cat == "Investment"||
            cat == "Debt"||
            cat == "Refund"||
            cat == "Salary"||
            cat == "Savings"||
            cat == "Business Profit"){

            if(cat == "Cash Withdrawal" ||
                cat == "Cost and Taxes" ||
                cat == "Insurance" ||
                cat == "Debt" ||
                cat == "Investment"){
                holder._circle.setBackgroundResource(R.drawable.ellipse_red)
                }
            else {

                holder._circle.setBackgroundResource(R.drawable.elilipse_green)
            }
            holder._logo.setBackgroundResource(R.drawable.icon_money)
        }
        else if(cat == "Entertainment" ||
                cat == "Books" ||
                cat == "Cinema" ||
                cat == "Fashion" ||
                cat == "Hobby" ||
                cat == "Sports" ||
                cat == "Games" ||
                cat == "Outgoing"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_entertainment)
        }
        else if(cat == "Cafe"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_cafe)
        }
        else if(cat == "Doctor Fee" ||
                cat == "Drugs/Medicine"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_medicine)
        }
        else if(cat == "Food and Drinks" ||
                cat == "Utilities" ||
                cat == "Groceries"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_groceries)
        }
        else if (cat == "Mobile and Internet" ||
                cat == "Cable TV"||
                cat == "Gadget and Electronic"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_mobileinternet)
        }
        else if (cat == "Gas" ||
            cat == "Gas" ||
            cat == "Transportation" ||
            cat == "Vehicle Maintenance" ||
            cat == "Parking Fee"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_transportation)
        }
        else if (cat == "Children"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_children)
        }
        else if (cat == "Gifts"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_gift)
        }
        else if (cat == "Education"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_education)
        }
        else if (cat == "House and Apartment Rent" ||
            cat == "Housing" ||
            cat == "Renovation" ||
            cat == "Domestic Helper"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_rumah)
        }
        else if (cat == "Credit Card" ||
            cat == "Top up Wallet"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_creditcard)
        }
        else if (cat == "Personal Care"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_personalcare)
        }
        else if (cat == "Pet"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_pet)
        }
        else if (cat == "Wedding"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_red)
            holder._logo.setBackgroundResource(R.drawable.icon_wedding)
        }
        else if (cat == "Others"){
            holder._circle.setBackgroundResource(R.drawable.ellipse_purple)
            holder._logo.setBackgroundResource(R.drawable.icon_others)
        }


        if(itemDetails.kategori == "Account Receivable" ||
        itemDetails.kategori == "Additional Income" ||
        itemDetails.kategori == "Bonus" ||
        itemDetails.kategori == "Allowance" ||
        itemDetails.kategori == "Capital Gain" ||
        itemDetails.kategori == "Income" ||
        itemDetails.kategori == "Refund" ||
        itemDetails.kategori == "Salary" ||
        itemDetails.kategori == "Savings" ||
        itemDetails.kategori == "Business Profit" ){
            holder._jumlah.setTextColor(Color.parseColor("#3CAE5C"))
            holder._plus.visibility = View.VISIBLE
        }
        else if (cat == "Others"){
            holder._jumlah.setTextColor(Color.parseColor("black"))
        }
        else{
            holder._jumlah.setTextColor(Color.parseColor("#EA2F14"))
            holder._min.visibility = View.VISIBLE
        }
        holder._jumlah.setText("%,d".format(itemDetails.jumlah.toString().toInt()))
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

}



