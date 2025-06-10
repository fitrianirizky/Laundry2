package com.rizky.laundry2.transaksi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.R.id.btBayarNanti
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KonfirmasiTransaksiActivity : AppCompatActivity() {
    private lateinit var tvPELANGGAN_NAMA: TextView
    private lateinit var tvPELANGGAN_NOHP: TextView
    private lateinit var tvLAYANAN_NAMA: TextView
    private lateinit var tvLAYANAN_HARGA: TextView
    private lateinit var tvNOMINAL: TextView
    private lateinit var btBATAL: Button
    private lateinit var btPEMBAYARAN: Button
    private lateinit var rvLAYANAN_TAMBAHAN: RecyclerView
    private val dataList = mutableListOf<ModelTransaksiTambahan>()

    private var hargaLayanan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_konfirmasi_transaksi)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvPELANGGAN_NAMA = findViewById(R.id.tvPELANGGAN_NAMA)
        tvPELANGGAN_NOHP = findViewById(R.id.tvPELANGGAN_NOHP)
        tvLAYANAN_NAMA = findViewById(R.id.tvLAYANAN_NAMA)
        tvLAYANAN_HARGA = findViewById(R.id.tvLAYANAN_HARGA)
        tvNOMINAL = findViewById(R.id.tvNOMINAL)
        btBATAL = findViewById(R.id.btBATAL)
        btPEMBAYARAN = findViewById(R.id.btPEMBAYARAN)
        rvLAYANAN_TAMBAHAN = findViewById(R.id.rvLAYANAN_TAMBAHAN)

        btPEMBAYARAN.setOnClickListener {
            showMetodePembayaranDialog()
        }

        btBATAL.setOnClickListener {
            finish()
        }

        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "-"
        val noHP = intent.getStringExtra("noHP") ?: "-"
        val namaLayanan = intent.getStringExtra("namaLayanan") ?: "-"
        hargaLayanan = intent.getStringExtra("hargaLayanan") ?: "0"
        val dataTambahan =
            intent.getSerializableExtra("dataTambahan") as? ArrayList<ModelTransaksiTambahan>

        val hargaLayananInt = hargaLayanan?.toIntOrNull() ?: 0
        val formattedLayanan = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(hargaLayananInt)
        tvLAYANAN_HARGA.text = formattedLayanan
        val totalHargaTambahan = dataTambahan?.sumOf {
            it.hargaTambahan?.toIntOrNull() ?: 0
        } ?: 0
        val totalBayar = hargaLayananInt + totalHargaTambahan

        val formattedTotal = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(totalBayar)

        tvNOMINAL.text = formattedTotal

        tvPELANGGAN_NAMA.text = namaPelanggan
        tvPELANGGAN_NOHP.text = noHP
        tvLAYANAN_NAMA.text = namaLayanan
        tvLAYANAN_HARGA.text = formattedLayanan

        dataTambahan?.let {
            dataList.addAll(it)
        }

        rvLAYANAN_TAMBAHAN.layoutManager = LinearLayoutManager(this)
        rvLAYANAN_TAMBAHAN.adapter = TambahanAdapter(dataList)
    }

    // Buat ViewHolder dan Adapter RecyclerView di sini supaya gak error
    inner class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTambahan = itemView.findViewById<TextView>(R.id.tvCARD_TAMBAHAN_ID)
        private val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_NAMA)
        private val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_HARGA)
        private val ivHapus: ImageView = itemView.findViewById(R.id.ivTrash)

        init {
            ivHapus.visibility = View.GONE
        }

        fun bind(item: ModelTransaksiTambahan) {
            idTambahan.text = "[${position + 1}]"
            tvNamaTambahan.text = item.namaTambahan ?: "-"
            val harga = item.hargaTambahan?.toIntOrNull() ?: 0
            val formattedHarga = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(harga)
            tvHargaTambahan.text = formattedHarga
        }
    }

    inner class TambahanAdapter(private val list: List<ModelTransaksiTambahan>) :
        RecyclerView.Adapter<TambahanViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
            val view = layoutInflater.inflate(R.layout.card_layanan_tambahan, parent, false)
            return TambahanViewHolder(view)
        }

        override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int = list.size
    }

    @SuppressLint("MissingInflatedId")
    private fun showMetodePembayaranDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mod_pembayaran, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        fun getCurrentTimestamp(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }

        fun simpanLaporan(metode: String) {
            val namaPelanggan = tvPELANGGAN_NAMA.text.toString().replace("Nama Pelanggan : ", "")
            val noHP = tvPELANGGAN_NOHP.text.toString().replace("No HP : ", "")
            val layanan = tvLAYANAN_NAMA.text.toString().replace("Nama Layanan : ", "")
            val totalBayar = tvNOMINAL.text.toString().replace("Rp", "").replace(",00", "").replace(".", "").trim()
            val tambahan = dataList.map { it.namaTambahan ?: "-" }

            val laporanRef = FirebaseDatabase.getInstance().getReference("laporan")
            val idLaporan = laporanRef.push().key ?: System.currentTimeMillis().toString()

            val laporanData = hashMapOf(
                "idLaporan" to idLaporan,
                "nama" to namaPelanggan,
                "noHP" to noHP,
                "layanan" to layanan,
                "tambahan" to tambahan.joinToString(","),
                "totalBayar" to totalBayar,
                "metodePembayaran" to metode,
                "status" to if (metode == "Bayar Nanti") "Belum Dibayar" else "Sudah Dibayar",
                "timestamp" to getCurrentTimestamp()
            )

            laporanRef.child(idLaporan).setValue(laporanData).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, getString(R.string.data_disimpan), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    val intent = Intent(this, NotaTransaksiActivity::class.java).apply {
                        putExtra("id", idLaporan)
                        putExtra("nama", namaPelanggan)
                        putExtra("noHP", noHP)
                        putExtra("layanan", layanan)
                        putExtra("hargaLayanan", hargaLayanan)
                        putExtra("totalBayar", totalBayar)
                        putExtra("metodePembayaran", metode)
                        putExtra("dataTambahan", ArrayList(dataList))
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.gagal_simpan), Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialogView.findViewById<Button>(R.id.btTunai).setOnClickListener {
            simpanLaporan("Tunai")
        }
        dialogView.findViewById<Button>(R.id.btQris).setOnClickListener {
            simpanLaporan("QRIS")
        }
        dialogView.findViewById<Button>(R.id.btDana).setOnClickListener {
            simpanLaporan("Dana")
        }
        dialogView.findViewById<Button>(R.id.btGoPay).setOnClickListener {
            simpanLaporan("GoPay")
        }
        dialogView.findViewById<Button>(R.id.btOvo).setOnClickListener {
            simpanLaporan("OVO")
        }
        dialogView.findViewById<Button>(btBayarNanti).setOnClickListener {
            simpanLaporan("Bayar Nanti")
        }
        dialogView.findViewById<TextView>(R.id.tvBatal).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
