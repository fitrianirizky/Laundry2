package com.rizky.laundry2.transaksi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelNotaTransaksi
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class NotaTransaksiActivity : AppCompatActivity() {
    private lateinit var tvIdTransaksi: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var tvPesanan: TextView
    private lateinit var tvKaryawan: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHargaLayanan: TextView
    private lateinit var tvTambahan: TextView
    private lateinit var rvTambahan: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvNominalSubtotal: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var tvNominalBayar: TextView
    private lateinit var tvCabang: TextView
    private lateinit var btKirim: Button
    private lateinit var btCetak: Button

    private val dataListTambahan = mutableListOf<ModelTransaksiTambahan>()
    private lateinit var notaData: ModelNotaTransaksi
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_transaksi)

        // Inisialisasi SharedPreferences
        sharedPref = getSharedPreferences("LOGIN_PREFS", Context.MODE_PRIVATE)

        initViews()
        setupRecyclerView()
        getIntentData()
        displayData()
        setupButtons()
    }

    private fun initViews() {
        tvIdTransaksi = findViewById(R.id.tvketIdTransaksi)
        tvTanggal = findViewById(R.id.tvketTanggal)
        tvPesanan = findViewById(R.id.tvketPesanan)
        tvKaryawan = findViewById(R.id.tvketKaryawan)
        tvLayanan = findViewById(R.id.tvLayanan)
        tvHargaLayanan = findViewById(R.id.tvHargaLayanan)
        tvTambahan = findViewById(R.id.tvTambahan)
        rvTambahan = findViewById(R.id.rvTambahan)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvNominalSubtotal = findViewById(R.id.tvNominalSubtotal)
        tvTotalBayar = findViewById(R.id.tvTotalBayar)
        tvNominalBayar = findViewById(R.id.tvNominalBayar)
        tvCabang = findViewById(R.id.tvcabang)
        btKirim = findViewById(R.id.btKirim)
        btCetak = findViewById(R.id.btCetak)
    }

    private fun setupRecyclerView() {
        rvTambahan.layoutManager = LinearLayoutManager(this)
        rvTambahan.adapter = TambahanAdapter(dataListTambahan)
    }

    private fun getIntentData() {
        val id = intent.getStringExtra("id") ?: generateTransactionId()
        val nama = intent.getStringExtra("nama") ?: "-"
        val noHP = intent.getStringExtra("noHP") ?: "-"
        val layanan = intent.getStringExtra("layanan") ?: "-"
        val hargaLayanan = intent.getStringExtra("hargaLayanan") ?: "0"
        val totalBayar = intent.getStringExtra("totalBayar") ?: "0"
        val metodePembayaran = intent.getStringExtra("metodePembayaran") ?: "-"
        val dataTambahan = intent.getSerializableExtra("dataTambahan") as? ArrayList<ModelTransaksiTambahan>

        // Ambil nama karyawan dari SharedPreferences (yang sudah disimpan saat login)
        val namaKaryawan = sharedPref.getString("nama", "Karyawan") ?: "Karyawan"
        val namaCabang = sharedPref.getString("cabang", "Cabang") ?: "Cabang"

        dataTambahan?.let {
            dataListTambahan.addAll(it)
        }

        notaData = ModelNotaTransaksi(
            idTransaksi = id,
            tanggal = getCurrentDateTime(),
            namaPelanggan = nama,
            namaKaryawan = namaKaryawan,
            layanan = layanan,
            hargaLayanan = hargaLayanan,
            tambahan = dataTambahan,
            totalBayar = totalBayar,
            metodePembayaran = metodePembayaran,
            cabang = namaCabang,
        )
    }

    private fun displayData() {
        tvIdTransaksi.text = notaData.idTransaksi
        tvTanggal.text = notaData.tanggal
        tvPesanan.text = notaData.namaPelanggan
        tvKaryawan.text = notaData.namaKaryawan
        tvLayanan.text = notaData.layanan
        tvCabang.text = notaData.cabang

        val harga = notaData.hargaLayanan?.toIntOrNull() ?: 0
        val formattedHarga = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(harga)
        tvHargaLayanan.text = formattedHarga

        // Hitung subtotal tambahan
        val subtotal = notaData.tambahan?.sumOf { it.hargaTambahan?.toIntOrNull() ?: 0 } ?: 0
        val formattedSubtotal = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(subtotal)
        tvNominalSubtotal.text = formattedSubtotal

        // Total bayar
        val total = notaData.totalBayar?.toIntOrNull() ?: 0
        val formattedTotal = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(total)
        tvNominalBayar.text = formattedTotal
    }

    private fun setupButtons() {
        btKirim.setOnClickListener {
            kirimWhatsApp()
        }

        btCetak.setOnClickListener {
            Toast.makeText(this, "Fitur cetak akan datang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun kirimWhatsApp() {
        val phoneNumber = ""
        val message = """
            *NOTA LAUNDRY*
            
            ID Transaksi: ${notaData.idTransaksi}
            Tanggal: ${notaData.tanggal}
            Pelanggan: ${notaData.namaPelanggan}
            Karyawan: ${notaData.namaKaryawan}
            
            Layanan: ${notaData.layanan}
            Harga: ${tvHargaLayanan.text}
            
            Tambahan:
            ${notaData.tambahan?.joinToString("\n") { "- ${it.namaTambahan}: ${formatCurrency(it.hargaTambahan?.toIntOrNull() ?: 0)}" } ?: "-"}
            
            Subtotal: ${tvNominalSubtotal.text}
            *Total Bayar: ${tvNominalBayar.text}*
            
            Metode Pembayaran: ${notaData.metodePembayaran}
            
            Terima kasih telah menggunakan layanan kami.
        """.trimIndent()

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}")
        startActivity(intent)
    }

    private fun generateTransactionId(): String {
        return "TRX-${System.currentTimeMillis()}"
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatCurrency(amount: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount)
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

    inner class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_NAMA)
        private val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_HARGA)
        private val ivHapus: ImageView = itemView.findViewById(R.id.ivTrash)

        init {
            ivHapus.visibility = View.GONE
        }

        fun bind(item: ModelTransaksiTambahan) {
            tvNamaTambahan.text = item.namaTambahan ?: "-"
            val harga = item.hargaTambahan?.toIntOrNull() ?: 0
            val formattedHarga = formatCurrency(harga)
            tvHargaTambahan.text = formattedHarga
        }
    }
}