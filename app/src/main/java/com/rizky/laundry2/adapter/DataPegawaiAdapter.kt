package com.rizky.laundry2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelPegawai

class DataPegawaiAdapter (
    private val listPegawai: ArrayList<ModelPegawai>) :
        RecyclerView.Adapter<DataPegawaiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pegawai = listPegawai[position]
        holder.tvCARD_PEGAWAI_ID.text = pegawai.idPegawai
        holder.tvCARD_PEGAWAI_NAMA.text = pegawai.namaPegawai
        holder.tvCARD_PEGAWAI_ALAMAT.text = pegawai.alamatPegawai
        holder.tvCARD_PEGAWAI_NOHP.text = pegawai.noHPPegawai
        holder.tvCARD_PEGAWAI_CABANG.text = pegawai.cabangPegawai

        holder.cvCARD_PEGAWAI.setOnClickListener {

        }
        holder.btHUBUNGI.setOnClickListener {

        }
        holder.btLIHAT.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_PEGAWAI: CardView = itemView.findViewById(R.id.cvCARD_PEGAWAI)
        val tvCARD_PEGAWAI_ID: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_ID)
        val tvCARD_PEGAWAI_NAMA: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_NAMA)
        val tvCARD_PEGAWAI_ALAMAT: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_ALAMAT)
        val tvCARD_PEGAWAI_NOHP: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_NOHP)
        val tvCARD_PEGAWAI_CABANG: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_CABANG)
        val btHUBUNGI: Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT: Button = itemView.findViewById(R.id.btLIHAT)
    }
}
