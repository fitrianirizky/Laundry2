package com.rizky.laundry2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelLayanan

class DataLayananAdapter (
    private val listLayanan: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<DataLayananAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = listLayanan[position]
        holder.tvCARD_LAYANAN_ID.text = layanan.idLayanan
        holder.tvCARD_LAYANAN_NAMA.text = layanan.namaLayanan
        holder.tvCARD_LAYANAN_HARGA.text = layanan.hargaLayanan.toString()
        holder.tvCARD_LAYANAN_CABANG.text = layanan.namaCabang

        holder.cvCARD_LAYANAN.setOnClickListener{

        }

        holder.btHUBUNGI.setOnClickListener{

        }
        holder.btLIHAT.setOnClickListener{

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
        val btHUBUNGI : Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT : Button = itemView.findViewById(R.id.btLIHAT)
    }

}
