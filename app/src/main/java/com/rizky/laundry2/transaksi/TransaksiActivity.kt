package com.rizky.laundry2.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.FirebaseApp
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan
import java.text.NumberFormat
import java.util.Locale

class TransaksiActivity : AppCompatActivity() {
    private lateinit var tvDATA_PELANGGAN : TextView
    private lateinit var tvPELANGGAN_NAMA : TextView
    private lateinit var tvPELANGGAN_NOHP : TextView
    private lateinit var btPILIH_PELANGGAN : Button
    private lateinit var tvLAYANAN_UTAMA : TextView
    private lateinit var tvLAYANAN_NAMA : TextView
    private lateinit var tvLAYANAN_HARGA : TextView
    private lateinit var btPILIH_LAYANAN : Button
    private lateinit var tvLAYANAN_TAMBAHAN : TextView
    private lateinit var rvLAYANAN_TAMBAHAN : RecyclerView
    private lateinit var ivTrash : ImageView
    private lateinit var btTAMBAHAN : Button
    private lateinit var btPROSES : Button
    private val dataList = mutableListOf<ModelTransaksiTambahan>()

    private var pilihPelanggan = 1
    private var pilihLayanan = 2
    private var pilihLayananTambahan = 3

    private var idPelanggan : String=""
    private var idCabang : String=""
    private var namaPelanggan : String=""
    private var noHP : String=""
    private var idLayanan : String=""
    private var namaLayanan : String=""
    private var hargaLayanan : String=""
    private var idPegawai : String=""
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaksi)
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        idCabang = sharedPref.getString("idCabang", null).toString()
        idPegawai = sharedPref.getString("idPegawai", null).toString()
        init()

        rvLAYANAN_TAMBAHAN.adapter = object : RecyclerView.Adapter<TambahanViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
                val view = layoutInflater.inflate(R.layout.card_layanan_tambahan, parent, false)
                return TambahanViewHolder(view)
            }

            override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
                val item = dataList[position]
                holder.bind(item, position) { pos ->
                    dataList.removeAt(pos as Int as Int)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, dataList.size)
                }
            }

            override fun getItemCount(): Int = dataList.size
        }


        FirebaseApp.initializeApp(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        rvLAYANAN_TAMBAHAN.layoutManager = layoutManager
        rvLAYANAN_TAMBAHAN.setHasFixedSize(true)
        btPILIH_PELANGGAN.setOnClickListener {
            val intent = Intent(this, PilihPelangganActivity::class.java)
            startActivityForResult(intent,pilihPelanggan)
        }
        btPILIH_LAYANAN.setOnClickListener {
            val intent = Intent(this, PilihLayananActivity::class.java)
            startActivityForResult(intent,pilihLayanan)
        }
        btTAMBAHAN.setOnClickListener {
            val intent = Intent(this, PilihLayananTambahanActivity::class.java)
            startActivityForResult(intent,pilihLayananTambahan)
        }
        btPROSES.setOnClickListener {
            if (namaPelanggan.isNotEmpty() && noHP.isNotEmpty() && namaLayanan.isNotEmpty()) {

                if (idPelanggan.isEmpty() || idLayanan.isEmpty()) {
                    Toast.makeText(this, getString(R.string.incomplete_data), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val intent = Intent(this, KonfirmasiTransaksiActivity::class.java)
                intent.putExtra("idPelanggan", idPelanggan)
                intent.putExtra("namaPelanggan", namaPelanggan)
                intent.putExtra("noHP", noHP)
                intent.putExtra("idLayanan", idLayanan)
                intent.putExtra("namaLayanan", namaLayanan)
                intent.putExtra("hargaLayanan", hargaLayanan)
                intent.putExtra("idPegawai", idPegawai)
                intent.putExtra("idCabang", idCabang)
                intent.putExtra("dataTambahan", ArrayList(dataList))
                startActivity(intent)
            }else {
                Toast.makeText(this, getString(R.string.please_complete_data), Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tvDATA_PELANGGAN = findViewById(R.id.tvDATA_PELANGGAN)
        tvPELANGGAN_NAMA = findViewById(R.id.tvPELANGGAN_NAMA)
        tvPELANGGAN_NOHP = findViewById(R.id.tvPELANGGAN_NOHP)
        btPILIH_PELANGGAN = findViewById(R.id.btPILIH_PELANGGAN)
        tvLAYANAN_UTAMA = findViewById(R.id.tvLAYANAN_UTAMA)
        tvLAYANAN_NAMA = findViewById(R.id.tvLAYANAN_NAMA)
        tvLAYANAN_HARGA = findViewById(R.id.tvLAYANAN_HARGA)
        btPILIH_LAYANAN = findViewById(R.id.btPILIH_LAYANAN)
        tvLAYANAN_TAMBAHAN = findViewById(R.id.tvLAYANAN_TAMBAHAN)
        rvLAYANAN_TAMBAHAN = findViewById(R.id.rvLAYANAN_TAMBAHAN)
        ivTrash= findViewById(R.id.ivTrash)
        btTAMBAHAN = findViewById(R.id.btTAMBAHAN)
        btPROSES = findViewById(R.id.btPROSES)
    }

    class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTambahan = itemView.findViewById<TextView>(R.id.tvCARD_TAMBAHAN_ID)
        private val nama = itemView.findViewById<TextView>(R.id.tvCARD_TAMBAHAN_NAMA)
        private val harga = itemView.findViewById<TextView>(R.id.tvCARD_TAMBAHAN_HARGA)
        private val hapus = itemView.findViewById<ImageView>(R.id.ivTrash)


        fun bind(item: ModelTransaksiTambahan, position: Int, param: (Any) -> Unit) {
            idTambahan.text = "[${position + 1}]"
            nama.text = item.namaTambahan
            harga.text = itemView.context.getString(R.string.label_harga, formatRupiah(item.hargaTambahan))

            hapus.visibility = View.VISIBLE
            hapus.setOnClickListener {
                param(position)
        }

        }

        private fun formatRupiah(amount: String?): String {
            return try {
                val numericValue = amount?.toDouble() ?: 0.0
                NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(numericValue)
            } catch (e: NumberFormatException) {
                "Rp0"
            }
        }
    }


    @SuppressLint("StringFormatInvalid")
    @Deprecated("This method has been deprecated in favor of using the Activity Result")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pilihPelanggan){
            if (resultCode == RESULT_OK && data != null){
                idPelanggan = data.getStringExtra("idPelanggan").toString()
                val nama = data.getStringExtra("nama")
                val nomorHP = data.getStringExtra("noHP")
                tvPELANGGAN_NAMA.text = getString(R.string.nama_pelanggan, nama)
                tvPELANGGAN_NOHP.text = getString(R.string.label_telepon, nomorHP)
                namaPelanggan = nama.toString()
                noHP = nomorHP.toString()
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.batal_pilih_pelanggan), Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == pilihLayanan){
            if(resultCode == RESULT_OK && data != null) {
                idLayanan = data.getStringExtra("idLayanan").toString()
                val nama = data.getStringExtra("nama")
                val harga = data.getStringExtra("harga")
                tvLAYANAN_NAMA.text = getString(R.string.nama_layanan, nama)
                tvLAYANAN_HARGA.text = getString(R.string.label_harga, formatRupiah(harga))
                namaLayanan = nama.toString()
                hargaLayanan = harga.toString()
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.batal_pilih_layanan), Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == pilihLayananTambahan){
            if(resultCode == RESULT_OK && data != null){
                val id = data.getStringExtra("idTambahan") ?: return
                val nama = data.getStringExtra("nama") ?: ""
                val harga = data.getStringExtra("harga") ?: ""

                val model = ModelTransaksiTambahan(id, nama, harga)

                dataList.add(model)
                rvLAYANAN_TAMBAHAN.adapter?.notifyDataSetChanged()
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.batal_pilih_tambahan), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun formatRupiah(amount: String?): String {
        return try {
            val numericValue = amount?.toDouble() ?: 0.0
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(numericValue)
        } catch (e: NumberFormatException) {
            "Rp0"
        }
    }

}