package com.rizky.laundry2.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan
import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale

class TambahanAdapter (
    private val tambahanList: ArrayList<ModelTransaksiTambahan>
) : RecyclerView.Adapter<TambahanAdapter.ViewHolder>() {

    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    private var onItemClickListener: ((ModelTransaksiTambahan, Int) -> Unit) ? = null

    fun setOnItemClickListener(listener: (ModelTransaksiTambahan, Int) -> Unit) {
        this.onItemClickListener = listener
    }

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val idTambahan = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_ID)
        val nama = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_NAMA)
        val harga = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_HARGA)
        val hapus = itemView.findViewById<android.widget.ImageView>(R.id.ivTrash)
        val cvTambahan = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cvCARD_TAMBAHAN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layanan_tambahan, parent, false)
        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("tambahan")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = tambahanList[position]
        holder.idTambahan.text = "[$nomor]"
        holder.nama.text = item.namaTambahan
        holder.harga.text = appContext.getString(R.string.label_harga, formatRupiah(item.hargaTambahan))

        holder.cvTambahan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("idTambahan", item.idTambahan)
            intent.putExtra("nama", item.namaTambahan)
            intent.putExtra("harga", item.hargaTambahan)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }

        if (appContext is TransaksiActivity) {
            holder.hapus.visibility = View.VISIBLE
            holder.hapus.setOnClickListener {
                onItemClickListener?.invoke(item, position)
            }
        } else {
            holder.hapus.visibility = View.GONE
        }

    }

    override fun getItemCount() = tambahanList.size

    fun updatedata(newData: List<ModelTransaksiTambahan>) {
        tambahanList.clear()
        tambahanList.addAll(newData)
        notifyDataSetChanged()
    }

    private fun formatRupiah(amount: String?): String {
        return try {
            val numericValue = amount?.toDouble() ?: 0.0
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(numericValue)
        } catch (e: NumberFormatException) {
            "Rp0"
        }
    }
}
