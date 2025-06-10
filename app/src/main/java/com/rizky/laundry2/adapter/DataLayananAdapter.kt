package com.rizky.laundry2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.layanan.TambahLayananActivity
import com.rizky.laundry2.modeldata.ModelLayanan
import java.text.NumberFormat
import java.util.Locale

class DataLayananAdapter (
    private val listLayanan: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<DataLayananAdapter.ViewHolder>(){
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanan, parent, false)
        appContext = parent.context

        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = listLayanan[position]
        holder.tvCARD_LAYANAN_ID.text = appContext.getString(R.string.label_id, layanan.idLayanan)
        holder.tvCARD_LAYANAN_NAMA.text = (layanan.namaLayanan)
        holder.tvCARD_LAYANAN_HARGA.text = appContext.getString(R.string.label_harga, formatCurrency(layanan.hargaLayanan))
        holder.tvCARD_LAYANAN_CABANG.text = appContext.getString(R.string.label_cabang, layanan.namaCabang)

        holder.cvCARD_LAYANAN.setOnClickListener{
            val intent = Intent(appContext, TambahLayananActivity::class.java)
            intent.putExtra("judul", appContext.getString(R.string.EditLayanan))
            intent.putExtra("idLayanan", layanan.idLayanan)
            intent.putExtra("namaLayanan", layanan.namaLayanan)
            intent.putExtra("hargaLayanan", layanan.hargaLayanan)
            intent.putExtra("namaCabang", layanan.namaCabang)
            appContext.startActivity(intent)
        }

    }

    private fun formatCurrency(amount: String?): String {
        return try {
            // Konversi String ke angka dulu
            val numericValue = amount?.toDouble() ?: 0.0
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(numericValue)
        } catch (e: NumberFormatException) {
            // Jika gagal konversi, tampilkan Rp0
            "Rp0"
        }
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_LAYANAN : CardView = itemView.findViewById(R.id.cvCARD_LAYANAN)
        val tvCARD_LAYANAN_NAMA : TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_NAMA)
        val tvCARD_LAYANAN_ID : TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_ID)
        val tvCARD_LAYANAN_HARGA : TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_HARGA)
        val tvCARD_LAYANAN_CABANG : TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_CABANG)
    }

}
