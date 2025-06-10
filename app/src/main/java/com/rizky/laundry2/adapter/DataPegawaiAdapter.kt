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
import com.rizky.laundry2.modeldata.ModelPegawai
import com.rizky.laundry2.pegawai.TambahPegawaiActivity
import java.text.SimpleDateFormat
import java.util.*

class DataPegawaiAdapter (
    private val listPegawai: ArrayList<ModelPegawai>) :
    RecyclerView.Adapter<DataPegawaiAdapter.ViewHolder>() {
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        appContext = parent.context

        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(view)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pegawai = listPegawai[position]
        holder.tvCARD_PEGAWAI_ID.text = appContext.getString(R.string.label_id, pegawai.idPegawai)
        holder.tvCARD_PEGAWAI_NAMA.text = (pegawai.namaPegawai)
        holder.tvCARD_PEGAWAI_ALAMAT.text = appContext.getString(R.string.label_alamat, pegawai.alamatPegawai)
        holder.tvCARD_PEGAWAI_NOHP.text = appContext.getString(R.string.label_telepon, pegawai.noHPPegawai)
        holder.tvCARD_PEGAWAI_CABANG.text = appContext.getString(R.string.label_cabang, pegawai.cabangPegawai)
        holder.tvCARD_PEGAWAI_TERDAFTAR.text =  formatTimestamp(pegawai.timestamp)

        holder.btHUBUNGI.setOnClickListener {
            val phoneNumber = pegawai.noHPPegawai
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
            val dialogView = inflater.inflate(R.layout.dialog_mod_pegawai, null)

            val dialogBuilder = android.app.AlertDialog.Builder(appContext)
                .setView(dialogView)
                .setCancelable(true)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            // Set Data ke View
            dialogView.findViewById<TextView>(R.id.tvjudul4).text = pegawai.idPegawai
            dialogView.findViewById<TextView>(R.id.tvjudul6).text = pegawai.namaPegawai
            dialogView.findViewById<TextView>(R.id.tvjudul8).text = pegawai.alamatPegawai
            dialogView.findViewById<TextView>(R.id.tvjudul10).text = pegawai.noHPPegawai
            dialogView.findViewById<TextView>(R.id.tvjudul12).text = pegawai.cabangPegawai
            dialogView.findViewById<TextView>(R.id.tvjudul14).text = formatTimestamp(pegawai.timestamp)

            // Fungsi tombol Sunting
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_PEGAWAI_EDIT).setOnClickListener {
                alertDialog.dismiss()
                val intent = Intent(appContext, TambahPegawaiActivity::class.java)
                intent.putExtra("judul", appContext.getString(R.string.EditPegawai))
                intent.putExtra("idPegawai", pegawai.idPegawai)
                intent.putExtra("namaPegawai", pegawai.namaPegawai)
                intent.putExtra("noHPPegawai", pegawai.noHPPegawai)
                intent.putExtra("alamatPegawai", pegawai.alamatPegawai)
                intent.putExtra("cabangPegawai", pegawai.cabangPegawai)
                appContext.startActivity(intent)
            }

            // Fungsi tombol Hapus
            dialogView.findViewById<Button>(R.id.btDIALOG_MOD_PEGAWAI_HAPUS).setOnClickListener {
                alertDialog.dismiss()
                // Tambahkan konfirmasi hapus
                android.app.AlertDialog.Builder(appContext)
                    .setTitle(appContext.getString(R.string.HapusKonfirmasi))
                    .setMessage(appContext.getString(R.string.KonfirmasiHapusPegawai))
                    .setPositiveButton(appContext.getString(R.string.Ya)) { _, _ ->
                        // Lakukan proses hapus dari Firebase
                        val id = pegawai.idPegawai ?: return@setPositiveButton
                        databaseReference.child("pegawai").child(id).removeValue()
                    }
                    .setNegativeButton(appContext.getString(R.string.Batal), null)
                    .show()
            }
        }

        holder.cvCARD_PEGAWAI.setOnClickListener{
            val intent = Intent(appContext, TambahPegawaiActivity::class.java)
            intent.putExtra("judul", appContext.getString(R.string.EditPegawai))
            intent.putExtra("idPegawai", pegawai.idPegawai)
            intent.putExtra("namaPegawai", pegawai.namaPegawai)
            intent.putExtra("noHPPegawai", pegawai.noHPPegawai)
            intent.putExtra("alamatPegawai", pegawai.alamatPegawai)
            intent.putExtra("cabangPegawai", pegawai.cabangPegawai)
            appContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    private fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return appContext.getString(R.string.tidak_ada_data)
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm:ss", locale)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_PEGAWAI: CardView = itemView.findViewById(R.id.cvCARD_PEGAWAI)
        val tvCARD_PEGAWAI_ID: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_ID)
        val tvCARD_PEGAWAI_NAMA: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_NAMA)
        val tvCARD_PEGAWAI_ALAMAT: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_ALAMAT)
        val tvCARD_PEGAWAI_NOHP: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_NOHP)
        val tvCARD_PEGAWAI_CABANG: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_CABANG)
        val tvCARD_PEGAWAI_TERDAFTAR: TextView = itemView.findViewById(R.id.tvCARD_PEGAWAI_TERDAFTAR)
        val btHUBUNGI: Button = itemView.findViewById(R.id.btHUBUNGI)
        val btLIHAT: Button = itemView.findViewById(R.id.btLIHAT)
    }
}