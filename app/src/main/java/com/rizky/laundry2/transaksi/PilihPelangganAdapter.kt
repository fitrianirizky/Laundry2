package com.rizky.laundry2.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelPelanggan
import java.util.ArrayList

class PilihPelangganAdapter(
    private val pelangganList: ArrayList<ModelPelanggan>
) : RecyclerView.Adapter<PilihPelangganAdapter.ViewHolder>() {

    lateinit var appContext: Context

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvCARD_PELANGGAN_ID = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_PELANGGAN_ID)
        val tvCARD_PELANGGAN_NAMA = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_PELANGGAN_NAMA)
        val tvCARD_PELANGGAN_ALAMAT = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_PELANGGAN_ALAMAT)
        val tvCARD_PELANGGAN_NOHP = itemView.findViewById<android.widget.TextView>(R.id.tvCARD_PELANGGAN_NOHP)
        val cvCARD_PELANGGAN = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cvCARD_PELANGGAN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_data_pelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = pelangganList[position]
        holder.tvCARD_PELANGGAN_ID.text = "[$nomor]"
        holder.tvCARD_PELANGGAN_NAMA.text = item.namaPelanggan
        holder.tvCARD_PELANGGAN_ALAMAT.text = appContext.getString(R.string.label_alamat, item.alamatPelanggan)
        holder.tvCARD_PELANGGAN_NOHP.text = appContext.getString(R.string.label_telepon, item.noHPPelanggan)

        holder.cvCARD_PELANGGAN.setOnClickListener {
            val intent = Intent()
            intent.putExtra("idPelanggan", item.idPelanggan)
            intent.putExtra("nama", item.namaPelanggan)
            intent.putExtra("noHP", item.noHPPelanggan)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getItemCount() = pelangganList.size
}
