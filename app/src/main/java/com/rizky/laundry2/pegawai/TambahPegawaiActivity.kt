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
    
    var idpegawai: String=""
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)
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
    
    fun getData(){
        idpegawai = intent.getStringExtra("idPegawai").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaPegawai")
        val alamat = intent.getStringExtra("alamatPegawai")
        val hp = intent.getStringExtra("noHPPegawai")
        val cabang = intent.getStringExtra("cabangPegawai")
        tvTambah_Pegawai.text = judul
        etNama_Lengkap.setText(nama)
        etAlamat.setText(alamat)
        etNo_HP.setText(hp)
        etCabang.setText(cabang)
        if (!tvTambah_Pegawai.text.equals(this.getString(R.string.tvTambah_Pegawai))){
            if (judul.equals(getString(R.string.EditPegawai))){
                mati()
                btSIMPAN.text=getString(R.string.Sunting)
            }
        }else{
            hidup()
            etNama_Lengkap.requestFocus()
            btSIMPAN.text=getString(R.string.simpan)
        }
    }
    
    fun mati() {
        etNama_Lengkap.isEnabled=false
        etAlamat.isEnabled=false
        etNo_HP.isEnabled=false
        etCabang.isEnabled=false
    }

    fun hidup() {
        etNama_Lengkap.isEnabled=true
        etAlamat.isEnabled=true
        etNo_HP.isEnabled=true
        etCabang.isEnabled=true
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
        if (btSIMPAN.text.equals(getString(R.string.simpan))){
            simpan()
        }else if (btSIMPAN.text.equals(getString(R.string.Sunting))){
            hidup()
            etNama_Lengkap.requestFocus()
            btSIMPAN.text=getString(R.string.perbarui)
        }else if (btSIMPAN.text.equals(getString(R.string.perbarui))){
            update()
        }
    }

    fun update(){
        val pegawaiRef = database.getReference("pegawai").child(idpegawai)
        val data =ModelPegawai(
            idpegawai,
            etNama_Lengkap.text.toString(),
            etAlamat.text.toString(),
            etNo_HP.text.toString(),
            etCabang.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        //Buat Map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"] = data.namaPegawai.toString()
        updateData["alamatPegawai"] = data.alamatPegawai.toString()
        updateData["noHPPegawai"] = data.noHPPegawai.toString()
        updateData["cabangPegawai"] = data.cabangPegawai.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahPegawaiActivity, getString(R.string.sukses_update_pegawai), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@TambahPegawaiActivity, getString(R.string.gagal_update_pegawai), Toast.LENGTH_SHORT).show()
        }
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
            timestamp = System.currentTimeMillis()
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