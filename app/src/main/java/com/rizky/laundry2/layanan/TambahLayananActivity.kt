package com.rizky.laundry2.layanan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelLayanan

class TambahLayananActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var tvTambah_Layanan: TextView
    lateinit var tvLayanan_Nama: TextView
    lateinit var etLayanan_Nama: EditText
    lateinit var tvLayanan_Harga: TextView
    lateinit var etLayanan_Harga: EditText
    lateinit var tvNama_Cabang: TextView
    lateinit var etNama_Cabang: EditText
    lateinit var btSIMPAN: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)
        init()
        btSIMPAN.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tvTambah_Layanan = findViewById(R.id.tvTambah_Layanan)
        tvLayanan_Nama = findViewById(R.id.tvLayanan_Nama)
        etLayanan_Nama = findViewById(R.id.etLayanan_Nama)
        tvLayanan_Harga = findViewById(R.id.tvLayanan_Harga)
        etLayanan_Harga = findViewById(R.id.etLayanan_Harga)
        tvNama_Cabang = findViewById(R.id.tvNama_Cabang)
        etNama_Cabang = findViewById(R.id.etNama_Cabang)
        btSIMPAN = findViewById(R.id.btSIMPAN)
    }

    fun cekValidasi(){
        val layanan = etLayanan_Nama.text.toString()
        val harga = etLayanan_Harga.text.toString()
        val cabang = etNama_Cabang.text.toString()
        //validasi data
        if (layanan.isEmpty()){
            etLayanan_Nama.error=this.getString(R.string.validasi_nama_layanan)
            Toast.makeText(this@TambahLayananActivity, this.getString(R.string.validasi_nama_layanan), Toast.LENGTH_SHORT).show()
            etLayanan_Nama.requestFocus()
            return
        }

        if (harga.isEmpty()){
            etLayanan_Harga.error=this.getString(R.string.validasi_harga_layanan)
            Toast.makeText(this@TambahLayananActivity, this.getString(R.string.validasi_harga_layanan), Toast.LENGTH_SHORT).show()
            etLayanan_Harga.requestFocus()
            return
        }

        if (layanan.isEmpty()){
            etNama_Cabang.error=this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(this@TambahLayananActivity, this.getString(R.string.validasi_nama_cabang), Toast.LENGTH_SHORT).show()
            etNama_Cabang.requestFocus()
            return
        }
        simpan()
    }

    fun simpan(){
        val layananBaru = myRef.push()
        val layananId = layananBaru.key
        val data = ModelLayanan(
            layananId ?: "",
            etLayanan_Nama.text.toString(),
            etLayanan_Harga.text.toString(),
            etNama_Cabang.text.toString(),
            "timestamp"
        )
        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this@TambahLayananActivity, this.getString(R.string.sukses_simpan_layanan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@TambahLayananActivity, this.getString(R.string.gagal_simpan_layanan),Toast.LENGTH_SHORT).show()
            }
    }
}
