package com.rizky.laundry2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelTambahan
import com.rizky.laundry2.tambahan.TambahTambahanActivity
import java.text.NumberFormat
import java.util.Locale

class DataTambahanAdapter (
    private val listTambahan: ArrayList<ModelTambahan>):
    RecyclerView.Adapter<DataTambahanAdapter.ViewHolder>(){
        lateinit var appContext: Context
        lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahan, parent, false)
        appContext = parent.context

        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tambahan = listTambahan[position]
        holder.tvCARD_TAMBAHAN_ID.text = appContext.getString(R.string.label_id, tambahan.idTambahan)
        holder.tvCARD_TAMBAHAN_NAMA.text = (tambahan.namaTambahan)
        holder.tvCARD_TAMBAHAN_HARGA.text = appContext.getString(R.string.label_harga, formatCurrency(tambahan.hargaTambahan))
        holder.tvCARD_TAMBAHAN_CABANG.text = appContext.getString(R.string.label_cabang, tambahan.namaCabangLayananTambahan)
        holder.tvCARD_TAMBAHAN_STATUS.text = "Status: ${tambahan.statusLayananTambahan}"

        holder.cvCARD_TAMBAHAN.setOnClickListener{
            val intent = Intent(appContext, TambahTambahanActivity::class.java)
            intent.putExtra("judul", appContext.getString(R.string.EditLayananTambahan) )
            intent.putExtra("idTambahan", tambahan.idTambahan)
            intent.putExtra("namaTambahan", tambahan.namaTambahan)
            intent.putExtra("hargaTambahan", tambahan.hargaTambahan)
            intent.putExtra("namaCabangLayananTambahan", tambahan.namaCabangLayananTambahan)
            intent.putExtra("statusLayananTambahan", tambahan.statusLayananTambahan)
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
        return  listTambahan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_TAMBAHAN : CardView = itemView.findViewById(R.id.cvCARD_TAMBAHAN)
        val tvCARD_TAMBAHAN_ID : TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_ID)
        val tvCARD_TAMBAHAN_NAMA : TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_NAMA)
        val tvCARD_TAMBAHAN_HARGA : TextView= itemView.findViewById(R.id.tvCARD_TAMBAHAN_HARGA)
        val tvCARD_TAMBAHAN_CABANG : TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_CABANG)
        val tvCARD_TAMBAHAN_STATUS : TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_STATUS)
    }

}
