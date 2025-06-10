package com.rizky.laundry2.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelLaporan
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class LaporanAdapter(
    private val listLaporan: ArrayList<ModelLaporan>,
    private val context: Context
) : RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_data_laporan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val data = listLaporan[position]

        holder.tvIdLaporan.text = "[$nomor]"
        holder.tvNama.text = data.nama
        holder.tvLayanan.text = data.layanan
        holder.tvTambahan.text = formatTambahan(data.tambahan)
        holder.tvTimestamp.text = parseTimestamp(data.timestamp)

        val formattedBayar = formatRupiah(data.totalBayar)
        val status = data.status ?: "Belum Dibayar"

        // Reset visibility untuk menghindari duplikasi
        holder.btBayar.visibility = View.GONE
        holder.tvDiambil.visibility = View.GONE

        when (status) {
            "Belum Dibayar" -> {
                holder.tvStatus.apply {
                    text = context.getString(R.string.belum_dibayar)
                    setBackgroundResource(R.drawable.bg_status_merah)
                    setTextColor(context.getColor(R.color.red))
                }
                holder.btBayar.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.bayar_sekarang)
                    setBackgroundResource(R.drawable.bg_tombol_merah)
                    setTextColor(context.getColor(R.color.white))
                    setOnClickListener {
                        showPaymentDialog(context, data)
                    }
                }
                holder.tvBayar.text = formattedBayar
            }

            "Lunas" -> {
                holder.tvStatus.apply {
                    text = context.getString(R.string.sudah_dibayar)
                    setBackgroundResource(R.drawable.bg_status_kuning)
                    setTextColor(context.getColor(R.color.orange))
                }
                holder.btBayar.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.ambil_sekarang)
                    setBackgroundResource(R.drawable.bg_tombol_biru)
                    setTextColor(context.getColor(R.color.white))
                    setOnClickListener {
                        val waktuAmbil = getNow()
                        data.status = "Diambil"
                        data.waktuAmbil = waktuAmbil

                        val dbRef = FirebaseDatabase.getInstance().getReference("laporan")
                        dbRef.child(data.idLaporan ?: "").apply {
                            child("status").setValue("Diambil")
                            child("waktuAmbil").setValue(waktuAmbil)
                        }

                        holder.tvStatus.apply {
                            text = context.getString(R.string.belum_dibayar)
                            setBackgroundResource(R.drawable.bg_status_hijau)
                            setTextColor(context.getColor(R.color.green))
                        }
                        holder.btBayar.visibility = View.GONE
                        holder.tvDiambil.visibility = View.VISIBLE
                        holder.tvDiambil.text = context.getString(R.string.diambil_pada, parseTimestamp(data.waktuAmbil))
                        holder.tvBayar.text = formattedBayar

                        Toast.makeText(context, context.getString(R.string.barang_diambil), Toast.LENGTH_SHORT).show()

                    }
                }
                holder.tvBayar.text = formattedBayar
            }

            "Diambil" -> {
                holder.tvStatus.apply {
                    text = context.getString(R.string.Selesai)
                    setBackgroundResource(R.drawable.bg_status_hijau)
                    setTextColor(context.getColor(R.color.green))
                }
                holder.btBayar.visibility = View.GONE
                holder.tvDiambil.visibility = View.VISIBLE
                holder.tvDiambil.text = context.getString(R.string.diambil_pada, parseTimestamp(data.waktuAmbil))
                holder.tvBayar.text = formattedBayar
            }

            else -> {
                holder.tvStatus.text = status
                holder.btBayar.visibility = View.GONE
                holder.tvBayar.text = formattedBayar
            }
        }
    }

    private fun formatTambahan(tambahan: String?): String {
        return if (tambahan.isNullOrEmpty() || tambahan == "-") {
            "-"
        } else {
            val items = tambahan.split(",").filter { it.isNotBlank() }
            when {
                items.isEmpty() -> "-"
                items.size == 1 ->  context.getString(R.string.layanan_tambahan_1)
                else -> context.getString(R.string.layanan_tambahan_n, items.size)
            }
        }
    }

    private fun showPaymentDialog(context: Context, laporan: ModelLaporan) {
        val dialog = Dialog(context).apply {
            setContentView(R.layout.dialog_mod_pembayaran)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("laporan")

        fun processPayment(metode: String) {
            val waktu = getNow()
            val updates = mapOf(
                "metodePembayaran" to metode,
                "status" to "Lunas",
                "timestamp" to waktu
            )

            val laporanId = laporan.idLaporan ?: return

            dbRef.child(laporanId)
                .updateChildren(updates)
                .addOnCompleteListener { task ->
                    val message = if (task.isSuccessful) {
                        laporan.metodePembayaran = metode
                        laporan.status = "Lunas"
                        laporan.timestamp = waktu

                        val pos = listLaporan.indexOfFirst { it.idLaporan == laporanId }
                        if (pos != -1) notifyItemChanged(pos)

                        context.getString(R.string.pembayaran_berhasil)
                    } else {
                        context.getString(R.string.pembayaran_gagal, task.exception?.message)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
        }

        dialog.findViewById<Button>(R.id.btTunai)?.setOnClickListener { processPayment("Tunai") }
        dialog.findViewById<Button>(R.id.btQris)?.setOnClickListener { processPayment("QRIS") }
        dialog.findViewById<Button>(R.id.btDana)?.setOnClickListener { processPayment("DANA") }
        dialog.findViewById<Button>(R.id.btGoPay)?.setOnClickListener { processPayment("GoPay") }
        dialog.findViewById<Button>(R.id.btOvo)?.setOnClickListener { processPayment("OVO") }

        dialog.findViewById<Button>(R.id.btBayarNanti)?.setOnClickListener { dialog.dismiss() }
        dialog.findViewById<TextView>(R.id.tvBatal)?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun getNow(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatRupiah(angka: String?): String {
        return try {
            val number = angka?.toDoubleOrNull() ?: 0.0
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            format.currency = Currency.getInstance("IDR")
            format.format(number)
        } catch (e: Exception) {
            "Rp0,00"
        }
    }

    fun parseTimestamp(raw: String?): String {
        if (raw.isNullOrEmpty()) return "-"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(raw) ?: return "-"

            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }

    override fun getItemCount(): Int = listLaporan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdLaporan: TextView = itemView.findViewById(R.id.tvIdLaporan)
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvLayanan: TextView = itemView.findViewById(R.id.tvLayanan)
        val tvTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvBayar: TextView = itemView.findViewById(R.id.tvBayar)
        val btBayar: Button = itemView.findViewById(R.id.btBayar)
        val tvDiambil: TextView = itemView.findViewById(R.id.tvDiambil) // Tambahkan ini
    }
}