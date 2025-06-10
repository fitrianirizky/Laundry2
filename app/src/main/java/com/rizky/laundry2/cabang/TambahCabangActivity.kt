package com.rizky.laundry2.cabang

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
import com.rizky.laundry2.modeldata.ModelCabang

class TambahCabangActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var tvTambah_Cabang: TextView
    lateinit var tvNama_Cabang: TextView
    lateinit var etNama_Cabang: EditText
    lateinit var tvAlamat: TextView
    lateinit var etAlamat: EditText
    lateinit var tvNo_HP: TextView
    lateinit var etNo_HP: EditText
    lateinit var btSIMPAN : Button

    var idCabang: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)
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
        tvTambah_Cabang = findViewById(R.id.tvTambah_Cabang)
        tvNama_Cabang = findViewById(R.id.tvNama_Cabang)
        etNama_Cabang = findViewById(R.id.etNama_Cabang)
        tvAlamat = findViewById(R.id.tvAlamat)
        etAlamat = findViewById(R.id.etAlamat)
        tvNo_HP = findViewById(R.id.tvNo_HP)
        etNo_HP = findViewById(R.id.etNo_HP)
        btSIMPAN = findViewById(R.id.btSIMPAN)
    }

    fun getData() {
        idCabang = intent.getStringExtra("idCabang").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaCabang")
        val alamat = intent.getStringExtra("alamatCabang")
        val hp = intent.getStringExtra("noHPCabang")
        tvTambah_Cabang.text = judul
        etNama_Cabang.setText(nama)
        etAlamat.setText(alamat)
        etNo_HP.setText(hp)
        if (!tvTambah_Cabang.text.equals(this.getString(R.string.tvTambah_Cabang))){
            if (judul.equals(getString(R.string.EditCabang))){
                mati()
                btSIMPAN.text=getString(R.string.Sunting)
            }
        }else{
            hidup()
            etNama_Cabang.requestFocus()
            btSIMPAN.text=getString(R.string.simpan)
        }
    }

    fun mati(){
        etNama_Cabang.isEnabled=false
        etAlamat.isEnabled=false
        etNo_HP.isEnabled=false
    }

    fun hidup(){
        etNama_Cabang.isEnabled=true
        etAlamat.isEnabled=true
        etNo_HP.isEnabled=true
    }

    fun cekValidasi(){
        val nama = etNama_Cabang.text.toString()
        val alamat = etAlamat.text.toString()
        val noHP = etNo_HP.text.toString()
        //validasi data
        if (nama.isEmpty()){
            etNama_Cabang.error=this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(this@TambahCabangActivity, this.getString(R.string.validasi_nama_cabang), Toast.LENGTH_SHORT).show()
            etNama_Cabang.requestFocus()
            return
        }

        if (alamat.isEmpty()){
            etAlamat.error=this.getString(R.string.validasi_alamat_pegawai)
            Toast.makeText(this@TambahCabangActivity, this.getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
            etAlamat.requestFocus()
            return
        }

        if (noHP.isEmpty()){
            etNo_HP.error=this.getString(R.string.validasi_noHP_pegawai)
            Toast.makeText(this@TambahCabangActivity, this.getString(R.string.validasi_noHP_pegawai), Toast.LENGTH_SHORT).show()
            etNo_HP.requestFocus()
            return
        }
        if (btSIMPAN.text.equals(getString(R.string.simpan))){
            simpan()
        }else if (btSIMPAN.text.equals(getString(R.string.Sunting))){
            hidup()
            etNama_Cabang.requestFocus()
            btSIMPAN.text=getString(R.string.perbarui)
        }else if (btSIMPAN.text.equals(getString(R.string.perbarui))){
            update()
        }
    }

    fun update() {
        val cabangRef = database.getReference("cabang").child(idCabang)
        val data = ModelCabang(
            idCabang,
            etNama_Cabang.text.toString(),
            etAlamat.text.toString(),
            etNo_HP.text.toString()
        )

        //buat mapuntuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaCabang"] = data.namaCabang.toString()
        updateData["alamatCabang"] = data.alamatCabang.toString()
        updateData["noHPCabang"] = data.noHPCabang.toString()
        cabangRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahCabangActivity, getString(R.string.sukses_update_cabang), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@TambahCabangActivity, getString(R.string.gagal_update_cabang), Toast.LENGTH_SHORT).show()
        }
    }

    fun simpan(){
        val cabangBaru = myRef.push()
        val cabangId = cabangBaru.key

        val data = ModelCabang(
            cabangId ?: "",
            etNama_Cabang.text.toString(),
            etAlamat.text.toString(),
            etNo_HP.text.toString(),
        )
        cabangBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this@TambahCabangActivity, this.getString(R.string.sukses_simpan_cabang), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@TambahCabangActivity, this.getString(R.string.gagal_simpan_cabang),
                    Toast.LENGTH_SHORT).show()
            }
    }
}