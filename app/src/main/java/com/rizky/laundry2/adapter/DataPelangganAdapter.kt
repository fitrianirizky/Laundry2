package com.rizky.laundry2.adapter

import com.rizky.laundry2.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.modeldata.ModelPelanggan


class DataPelangganAdapter(
    private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<DataPelangganAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = listPelanggan[position]
        holder.tvCARD_PELANGGAN_ID.text = pelanggan.idPelanggan
        holder.tvCARD_PELANGGAN_NAMA.text = pelanggan.namaPelanggan
        holder.tvCARD_PELANGGAN_ALAMAT.text = pelanggan.alamatPelanggan
        holder.tvCARD_PELANGGAN_NOHP.text = pelanggan.noHPPelanggan

        holder.cvCARD_PELANGGAN.setOnClickListener{

        }
        holder.btHUBUNGI.setOnClickListener{

        }
        holder.btLIHAT.setOnClickListener{

        }

    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val cvCARD_PELANGGAN : CardView = itemView.findViewById(R.id.cvCARD_PELANGGAN)
       val tvCARD_PELANGGAN_ID : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_ID)
       val tvCARD_PELANGGAN_NAMA : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_NAMA)
        val tvCARD_PELANGGAN_ALAMAT : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_ALAMAT)
        val tvCARD_PELANGGAN_NOHP : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_NOHP)
        val btHUBUNGI : Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT : Button = itemView.findViewById(R.id.btLIHAT)
    }
}
