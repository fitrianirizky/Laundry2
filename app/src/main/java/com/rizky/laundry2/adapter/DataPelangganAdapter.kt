package com.rizky.laundry2.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.rizky.laundry2.R
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
import com.rizky.laundry2.modeldata.ModelPelanggan
import com.rizky.laundry2.pelanggan.TambahPelangganActivity
import java.text.SimpleDateFormat
import java.util.*


class DataPelangganAdapter(
    private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<DataPelangganAdapter.ViewHolder>() {
        lateinit var appContext: Context
        lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        appContext = parent.context

        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = listPelanggan[position]

        holder.tvCARD_PELANGGAN_ID.text = appContext.getString(R.string.label_id, pelanggan.idPelanggan)
        holder.tvCARD_PELANGGAN_NAMA.text = (pelanggan.namaPelanggan)
        holder.tvCARD_PELANGGAN_ALAMAT.text = appContext.getString(R.string.label_alamat, pelanggan.alamatPelanggan)
        holder.tvCARD_PELANGGAN_NOHP.text = appContext.getString(R.string.label_telepon, pelanggan.noHPPelanggan)
        holder.tvCARD_PELANGGAN_CABANG.text = appContext.getString(R.string.label_cabang, pelanggan.idCabang)
        holder.tvCARD_PELANGGAN_TERDAFTAR.text =  formatTimestamp(pelanggan.timestamp)

        holder.cvCARD_PELANGGAN.setOnClickListener{
            val intent = Intent(appContext, TambahPelangganActivity::class.java)
            intent.putExtra("judul", appContext.getString(R.string.EditPelanggan))
            intent.putExtra("idPelanggan", pelanggan.idPelanggan)
            intent.putExtra("namaPelanggan", pelanggan.namaPelanggan)
            intent.putExtra("alamatPelanggan", pelanggan.alamatPelanggan)
            intent.putExtra("noHPPelanggan", pelanggan.noHPPelanggan)
            intent.putExtra("idCabang", pelanggan.idCabang)
            appContext.startActivity(intent)
        }

        holder.btHUBUNGI.setOnClickListener {
            val phoneNumber = pelanggan.noHPPelanggan
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
            val dialogView = inflater.inflate(R.layout.dialog_mod_pelanggan, null)

            val dialogBuilder = android.app.AlertDialog.Builder(appContext)
                .setView(dialogView)
                .setCancelable(true)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            // Set Data ke View
            dialogView.findViewById<TextView>(R.id.tvjudul4).text = pelanggan.idPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul6).text = pelanggan.namaPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul8).text = pelanggan.alamatPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul10).text = pelanggan.noHPPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul12).text = pelanggan.idCabang
            dialogView.findViewById<TextView>(R.id.tvjudul14).text = formatTimestamp(pelanggan.timestamp)

            // Fungsi tombol Sunting
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_PELANGGAN_EDIT).setOnClickListener {
                alertDialog.dismiss()
                val intent = Intent(appContext, TambahPelangganActivity::class.java)
                intent.putExtra("judul", appContext.getString(R.string.EditPelanggan))
                intent.putExtra("idPelanggan", pelanggan.idPelanggan)
                intent.putExtra("namaPelanggan", pelanggan.namaPelanggan)
                intent.putExtra("alamatPelanggan", pelanggan.alamatPelanggan)
                intent.putExtra("noHPPelanggan", pelanggan.noHPPelanggan)
                intent.putExtra("idCabang", pelanggan.idCabang)
                appContext.startActivity(intent)
            }

            // Fungsi tombol Hapus
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_PELANGGAN_HAPUS).setOnClickListener {
                alertDialog.dismiss()
                // Tambahkan konfirmasi hapus
                android.app.AlertDialog.Builder(appContext)
                    .setTitle(appContext.getString(R.string.HapusKonfirmasi))
                    .setMessage(appContext.getString(R.string.KonfirmasiHapusPelanggan))
                    .setPositiveButton(appContext.getString(R.string.Ya)) { _, _ ->
                        // Lakukan proses hapus dari Firebase
                        val id = pelanggan.idPelanggan ?: return@setPositiveButton
                        databaseReference.child("pelanggan").child(id).removeValue()
                    }
                    .setNegativeButton(appContext.getString(R.string.Batal), null)
                    .show()
            }
        }

    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    private fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return appContext.getString(R.string.tidak_ada_data)
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm:ss", locale)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp))
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_PELANGGAN : CardView = itemView.findViewById(R.id.cvCARD_PELANGGAN)
        val tvCARD_PELANGGAN_ID : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_ID)
        val tvCARD_PELANGGAN_NAMA : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_NAMA)
        val tvCARD_PELANGGAN_ALAMAT : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_ALAMAT)
        val tvCARD_PELANGGAN_NOHP : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_NOHP)
        val tvCARD_PELANGGAN_CABANG : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_CABANG)
        val tvCARD_PELANGGAN_TERDAFTAR : TextView = itemView.findViewById(R.id.tvCARD_PELANGGAN_TERDAFTAR)
        val btHUBUNGI : Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT : Button = itemView.findViewById(R.id.btLIHAT)
    }
}