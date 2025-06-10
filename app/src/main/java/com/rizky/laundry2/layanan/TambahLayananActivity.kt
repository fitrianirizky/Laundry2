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

    var idLayanan: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)
        init()
        getData()
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

    fun getData(){
        idLayanan = intent.getStringExtra("idLayanan").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaLayanan")
        val harga = intent.getStringExtra("hargaLayanan")
        val cabang = intent.getStringExtra("namaCabang")
        tvTambah_Layanan.text = judul
        etLayanan_Nama.setText(nama)
        etLayanan_Harga.setText(harga)
        etNama_Cabang.setText(cabang)
        if (!tvTambah_Layanan.text.equals(this.getString(R.string.tvTambah_Layanan))){
            if (judul.equals(getString(R.string.EditLayanan))){
                mati()
                btSIMPAN.text=getString(R.string.Sunting)
            }
        }else{
            hidup()
            etLayanan_Nama.requestFocus()
            btSIMPAN.text=getString(R.string.simpan)
        }
    }

    fun mati(){
        etLayanan_Nama.isEnabled=false
        etLayanan_Harga.isEnabled=false
        etNama_Cabang.isEnabled=false
    }

    fun hidup(){
        etLayanan_Nama.isEnabled=true
        etLayanan_Harga.isEnabled=true
        etNama_Cabang.isEnabled=true
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

        if (cabang.isEmpty()){
            etNama_Cabang.error=this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(this@TambahLayananActivity, this.getString(R.string.validasi_nama_cabang), Toast.LENGTH_SHORT).show()
            etNama_Cabang.requestFocus()
            return
        }
        if (btSIMPAN.text.equals(getString(R.string.simpan))){
            simpan()
        }else if (btSIMPAN.text.equals(getString(R.string.Sunting))){
            hidup()
            etLayanan_Nama.requestFocus()
            btSIMPAN.text=getString(R.string.perbarui)
        }else if (btSIMPAN.text.equals(getString(R.string.perbarui))){
            update()
        }
    }

    fun update(){
        val layananRef = database.getReference("layanan").child(idLayanan)
        val data = ModelLayanan(
            idLayanan,
            etLayanan_Nama.text.toString(),
            etLayanan_Harga.toString(),
            etNama_Cabang.toString()
        )

        //Buat Map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaLayanan"] = data.namaLayanan.toString()
        updateData["hargaLayanan"] = data.hargaLayanan.toString()
        updateData["namaCabang"] = data.namaCabang.toString()
        layananRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahLayananActivity, getString(R.string.sukses_update_layanan), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@TambahLayananActivity, getString(R.string.gagal_update_layanan), Toast.LENGTH_SHORT).show()
        }
    }

    fun simpan(){
        val layananBaru = myRef.push()
        val layananId = layananBaru.key
        val data = ModelLayanan(
            layananId.toString(),
            etLayanan_Nama.text.toString(),
            etLayanan_Harga.text.toString(),
            etNama_Cabang.text.toString(),
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
