package com.rizky.laundry2.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelLayanan
import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale

class PilihLayananAdapter(
    private val layananList: ArrayList<ModelLayanan>
) : RecyclerView.Adapter<PilihLayananAdapter.ViewHolder>() {

    lateinit var appContext: Context

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val idLayanan = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_LAYANAN_ID)
        val nama = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_LAYANAN_NAMA)
        val harga = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_LAYANAN_HARGA)
        val cvLayanan = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cvCARD_PILIH_DATA_LAYANAN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_data_layanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = layananList[position]
        holder.idLayanan.text = "[$nomor]"
        holder.nama.text = item.namaLayanan
        holder.harga.text = appContext.getString(R.string.label_harga, formatRupiah(item.hargaLayanan))

        holder.cvLayanan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("idLayanan", item.idLayanan)
            intent.putExtra("nama", item.namaLayanan)
            intent.putExtra("harga", item.hargaLayanan)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    private fun formatRupiah(amount: String?): String {
        return try {
            val numericValue = amount?.toDouble() ?: 0.0
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(numericValue)
        } catch (e: NumberFormatException) {
            "Rp0"
        }
    }

    override fun getItemCount() = layananList.size
}
