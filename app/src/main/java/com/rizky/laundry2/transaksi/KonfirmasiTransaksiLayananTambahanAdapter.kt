package com.rizky.laundry2.transaksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan
import java.util.ArrayList

class KonfirmasiTransaksiLayananTambahanAdapter (
    private val tambahanList: ArrayList<ModelTransaksiTambahan>
) : RecyclerView.Adapter<KonfirmasiTransaksiLayananTambahanAdapter.ViewHolder>() {

    lateinit var appContext: Context

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val idTambahan = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_ID)
        val nama = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_NAMA)
        val harga = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_TAMBAHAN_HARGA)
        val hapus = itemView.findViewById<android.widget.ImageView>(R.id.ivTrash)
        init {
            hapus.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rizky.laundry2.transaksi.KonfirmasiTransaksiLayananTambahanAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layanan_tambahan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount() = tambahanList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = tambahanList[position]
        holder.idTambahan.text = "[$nomor]"
        holder.nama.text = item.namaTambahan
        holder.harga.text = "Harga : ${item.hargaTambahan}"
    }

}
