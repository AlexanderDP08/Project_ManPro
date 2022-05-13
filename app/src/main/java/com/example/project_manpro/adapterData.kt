package com.example.project_manpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        holder._jumlah.setText(itemDetails.jumlah)
        holder._kategori.setText(itemDetails.kategori)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

}



