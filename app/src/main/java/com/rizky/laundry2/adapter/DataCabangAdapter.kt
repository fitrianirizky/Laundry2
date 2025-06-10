package com.rizky.laundry2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.cabang.TambahCabangActivity
import com.rizky.laundry2.modeldata.ModelCabang


class DataCabangAdapter (
    private val listCabang: ArrayList<ModelCabang>) :
    RecyclerView.Adapter<DataCabangAdapter.ViewHolder>(){
        lateinit var appContext: Context
        lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabang, parent, false)
        appContext = parent.context

        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(view)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cabang = listCabang[position]
        holder.tvCARD_CABANG_ID.text = appContext.getString(R.string.label_id, cabang.idCabang)
        holder.tvCARD_CABANG_NAMA.text = cabang.namaCabang
        holder.tvCARD_CABANG_ALAMAT.text = appContext.getString(R.string.label_alamat, cabang.alamatCabang)
        holder.tvCARD_CABANG_NOHP.text = appContext.getString(R.string.label_telepon, cabang.noHPCabang)


        holder.btHUBUNGI.setOnClickListener {
            val phoneNumber = cabang.noHPCabang
                ?.replace("+", "") // Hilangkan tanda + kalau ada
                ?.replace(" ", "") // Hilangkan spasi

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$phoneNumber")

            try {
                appContext.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(appContext, appContext.getString(R.string.whatsapp_tidak_ditemukan), Toast.LENGTH_SHORT).show()
            }
        }

        holder.btLIHAT.setOnClickListener {
            val inflater = LayoutInflater.from(appContext)
            val dialogView = inflater.inflate(R.layout.dialog_mod_cabang, null)

            val dialogBuilder = android.app.AlertDialog.Builder(appContext)
                .setView(dialogView)
                .setCancelable(true)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            // Set Data ke View
            dialogView.findViewById<TextView>(R.id.tvjudul4).text = cabang.idCabang
            dialogView.findViewById<TextView>(R.id.tvjudul6).text = cabang.namaCabang
            dialogView.findViewById<TextView>(R.id.tvjudul8).text = cabang.alamatCabang
            dialogView.findViewById<TextView>(R.id.tvjudul10).text = cabang.noHPCabang

            // Fungsi tombol Sunting
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_CABANG_EDIT).setOnClickListener {
                alertDialog.dismiss()
                val intent = Intent(appContext, TambahCabangActivity::class.java)
                intent.putExtra("judul", appContext.getString(R.string.EditCabang))
                intent.putExtra("idCabang", cabang.idCabang)
                intent.putExtra("namaCabang", cabang.namaCabang)
                intent.putExtra("alamatCabang", cabang.alamatCabang)
                intent.putExtra("noHPCabang", cabang.noHPCabang)
                appContext.startActivity(intent)
            }

            // Fungsi tombol Hapus
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_CABANG_HAPUS).setOnClickListener {
                alertDialog.dismiss()
                // Tambahkan konfirmasi hapus
                android.app.AlertDialog.Builder(appContext)
                    .setTitle(appContext.getString(R.string.HapusKonfirmasi))
                    .setMessage(appContext.getString(R.string.KonfirmasiHapusCabang))
                    .setPositiveButton(appContext.getString(R.string.Ya)) { _, _ ->
                        // Lakukan proses hapus dari Firebase
                        val id = cabang.idCabang ?: return@setPositiveButton
                        databaseReference.child("cabang").child(id).removeValue()
                    }
                    .setNegativeButton(appContext.getString(R.string.Batal), null)
                    .show()
            }
        }

        holder.cvCARD_CABANG.setOnClickListener{
            val intent = Intent(appContext, TambahCabangActivity::class.java)
            intent.putExtra("judul", appContext.getString(R.string.EditCabang))
            intent.putExtra("idCabang", cabang.idCabang)
            intent.putExtra("namaCabang", cabang.namaCabang)
            intent.putExtra("alamatCabang", cabang.alamatCabang)
            intent.putExtra("noHPCabang", cabang.noHPCabang)
            appContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listCabang.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_CABANG : CardView = itemView.findViewById(R.id.cvCARD_CABANG)
        val tvCARD_CABANG_ID : TextView = itemView.findViewById(R.id.tvCARD_CABANG_ID)
        val tvCARD_CABANG_NAMA : TextView = itemView.findViewById(R.id.tvCARD_CABANG_NAMA)
        val tvCARD_CABANG_ALAMAT : TextView = itemView.findViewById(R.id.tvCARD_CABANG_ALAMAT)
        val tvCARD_CABANG_NOHP : TextView = itemView.findViewById(R.id.tvCARD_CABANG_NOHP)
        val btHUBUNGI : Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT : Button = itemView.findViewById(R.id.btLIHAT)
    }

}
