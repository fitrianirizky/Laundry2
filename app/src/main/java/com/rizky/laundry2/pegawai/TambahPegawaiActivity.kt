package com.rizky.laundry2.pegawai

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
import com.rizky.laundry2.modeldata.ModelPegawai

class TambahPegawaiActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var tvTambah_Pegawai: TextView
    lateinit var tvNama_Lengkap: TextView
    lateinit var etNama_Lengkap: EditText
    lateinit var tvAlamat: TextView
    lateinit var etAlamat: EditText
    lateinit var tvNo_HP: TextView
    lateinit var etNo_HP: EditText
    lateinit var tvCabang: TextView
    lateinit var etCabang: EditText
    lateinit var btSIMPAN : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)
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
        tvTambah_Pegawai = findViewById(R.id.tvTambah_Pegawai)
        tvNama_Lengkap = findViewById(R.id.tvNama_Lengkap)
        etNama_Lengkap = findViewById(R.id.etNama_Lengkap)
        tvAlamat = findViewById(R.id.tvAlamat)
        etAlamat = findViewById(R.id.etAlamat)
        tvNo_HP = findViewById(R.id.tvNo_HP)
        etNo_HP = findViewById(R.id.etNo_HP)
        tvCabang = findViewById(R.id.tvCabang)
        etCabang = findViewById(R.id.etCabang)
        btSIMPAN = findViewById(R.id.btSIMPAN)
    }

    fun cekValidasi(){
        val nama = etNama_Lengkap.text.toString()
        val alamat = etAlamat.text.toString()
        val noHP = etNo_HP.text.toString()
        val cabang = etCabang.text.toString()
        //validasi data
        if (nama.isEmpty()){
            etNama_Lengkap.error=this.getString(R.string.validasi_nama_pegawai)
            Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.validasi_nama_pegawai), Toast.LENGTH_SHORT).show()
            etNama_Lengkap.requestFocus()
            return
        }

        if (alamat.isEmpty()){
            etAlamat.error=this.getString(R.string.validasi_alamat_pegawai)
            Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
            etAlamat.requestFocus()
            return
        }

        if (noHP.isEmpty()){
            etNo_HP.error=this.getString(R.string.validasi_noHP_pegawai)
            Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.validasi_noHP_pegawai), Toast.LENGTH_SHORT).show()
            etNo_HP.requestFocus()
            return
        }

        if (cabang.isEmpty()){
            etCabang.error=this.getString(R.string.validasi_cabang_pegawai)
            Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.validasi_cabang_pegawai), Toast.LENGTH_SHORT).show()
            etCabang.requestFocus()
            return
        }
        simpan()
    }

    fun simpan(){
        val pegawaiBaru = myRef.push()
        val pegawaiId = pegawaiBaru.key
        val data = ModelPegawai(
            pegawaiId ?: "",
            etNama_Lengkap.text.toString(),
            etAlamat.text.toString(),
            etNo_HP.text.toString(),
            etCabang.text.toString(),
            "timestamp"
        )
        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.sukses_simpan_pegawai), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@TambahPegawaiActivity, this.getString(R.string.gagal_simpan_pegawai),Toast.LENGTH_SHORT).show()
            }
    }

}
