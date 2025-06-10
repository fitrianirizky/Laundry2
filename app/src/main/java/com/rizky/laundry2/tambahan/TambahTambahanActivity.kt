package com.rizky.laundry2.tambahan

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
import com.rizky.laundry2.modeldata.ModelTambahan

class TambahTambahanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val  myRef = database.getReference("tambahan")
    lateinit var tvTambahan_Tambahan: TextView
    lateinit var tvNama_Tambahan: TextView
    lateinit var etNama_Tambahan: EditText
    lateinit var tvHarga_Tambahan: TextView
    lateinit var etHarga_Tambahan: EditText
    lateinit var tvCabang_Tambahan: TextView
    lateinit var etCabang_Tambahan: EditText
    lateinit var tvStatus_Tambahan: TextView
    lateinit var etStatus_Tambahan: EditText
    lateinit var btSIMPAN: Button

    var idTambahan: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahan)
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
        tvTambahan_Tambahan = findViewById(R.id.tvTambahan_Tambahan)
        tvNama_Tambahan = findViewById(R.id.tvNama_Tambahan)
        etNama_Tambahan = findViewById(R.id.etNama_Tambahan)
        tvHarga_Tambahan = findViewById(R.id.tvHarga_Tambahan)
        etHarga_Tambahan = findViewById(R.id.etHarga_Tambahan)
        tvCabang_Tambahan = findViewById(R.id.tvCabang_Tambahan)
        etCabang_Tambahan = findViewById(R.id.etCabang_Tambahan)
        tvStatus_Tambahan = findViewById(R.id.tvStatus_Tambahan)
        etStatus_Tambahan = findViewById(R.id.etStatus_Tambahan)
        btSIMPAN= findViewById(R.id.btSIMPAN)
    }

    fun getData(){
        idTambahan = intent.getStringExtra("idTambahan").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaTambahan")
        val harga = intent.getStringExtra("hargaTambahan")
        val cabang = intent.getStringExtra("namaCabangLayananTambahan")
        val status = intent.getStringExtra("statuslayananTambahan")
        tvTambahan_Tambahan.text = judul
        etNama_Tambahan.setText(nama)
        etHarga_Tambahan.setText(harga)
        etCabang_Tambahan.setText(cabang)
        etStatus_Tambahan.setText(status)
        if (!tvTambahan_Tambahan.text.equals(this.getString(R.string.tvTambahan_Tambahan))){
            if (judul.equals(getString(R.string.EditTambahan))){
                mati()
                btSIMPAN.text=getString(R.string.Sunting)
            }
        }else {
            hidup()
            etNama_Tambahan.requestFocus()
            btSIMPAN.text =getString(R.string.simpan)
        }
    }

    fun mati(){
        etNama_Tambahan.isEnabled=false
        etHarga_Tambahan.isEnabled=false
        etCabang_Tambahan.isEnabled=false
        etStatus_Tambahan.isEnabled=false
    }

    fun hidup(){
        etNama_Tambahan.isEnabled=true
        etHarga_Tambahan.isEnabled=true
        etCabang_Tambahan.isEnabled=true
        etStatus_Tambahan.isEnabled=true
    }

    fun cekValidasi(){
        val nama = etNama_Tambahan.text.toString()
        val harga = etHarga_Tambahan.text.toString()
        val cabang = etCabang_Tambahan.text.toString()
        val status = etStatus_Tambahan.text.toString()

        //validasi data
        if (nama.isEmpty()){
            etNama_Tambahan.error=this.getString(R.string.validasi_nama_tambahan)
            Toast.makeText(this@TambahTambahanActivity, this.getString(R.string.validasi_nama_tambahan), Toast.LENGTH_SHORT).show()
            etNama_Tambahan.requestFocus()
            return
        }

        if (harga.isEmpty()){
            etHarga_Tambahan.error=this.getString(R.string.validasi_harga_tambahan)
            Toast.makeText(this@TambahTambahanActivity, this.getString(R.string.validasi_harga_tambahan), Toast.LENGTH_SHORT).show()
            etHarga_Tambahan.requestFocus()
            return
        }

        if (cabang.isEmpty()){
            etCabang_Tambahan.error=this.getString(R.string.validasi_cabang_tambahan)
            Toast.makeText(this@TambahTambahanActivity, this.getString(R.string.validasi_cabang_tambahan), Toast.LENGTH_SHORT).show()
            etCabang_Tambahan.requestFocus()
            return
        }

        if (status.isEmpty()){
            etStatus_Tambahan.error=this.getString(R.string.validasi_status_tambahan)
            Toast.makeText(this@TambahTambahanActivity, this.getString(R.string.validasi_status_tambahan), Toast.LENGTH_SHORT).show()
            etStatus_Tambahan.requestFocus()
            return
        }
        if (btSIMPAN.text.equals(getString(R.string.simpan))){
            simpan()
        }else if (btSIMPAN.text.equals(getString(R.string.Sunting))){
            hidup()
            etNama_Tambahan.requestFocus()
            btSIMPAN.text=getString(R.string.perbarui)
        }else if (btSIMPAN.text.equals(getString(R.string.perbarui))){
            update()
        }
    }

    fun update(){
        val tambahanRef = database.getReference("tambahan").child(idTambahan)
        val data = ModelTambahan(
            idTambahan,
            etNama_Tambahan.text.toString(),
            etHarga_Tambahan.text.toString(),
            etCabang_Tambahan.text.toString(),
            etStatus_Tambahan.text.toString()
        )

        //Buat Map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaTambahan"] = data.namaTambahan.toString()
        updateData["hargaTambahan"] = data.hargaTambahan.toString()
        updateData["namaCabangLayananTambahan"] = data.namaCabangLayananTambahan.toString()
        updateData["statuslayananTambahan"] = data.statusLayananTambahan.toString()
        tambahanRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahTambahanActivity, getString(R.string.sukses_update_tambahan), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@TambahTambahanActivity, getString(R.string.gagal_update_tambahan), Toast.LENGTH_SHORT).show()
        }
    }

    fun simpan(){
        val tambahanBaru = myRef.push()
        val tambahanId = tambahanBaru.key
        val data = ModelTambahan(
            tambahanId ?: "",
            etNama_Tambahan.text.toString(),
            etHarga_Tambahan.text.toString(),
            etCabang_Tambahan.text.toString(),
            etStatus_Tambahan.text.toString(),

        )
        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this@TambahTambahanActivity, this.getString(R.string.sukses_simpan_tambahan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@TambahTambahanActivity, getString(R.string.gagal_simpan_tambahan), Toast.LENGTH_SHORT).show()
            }
    }
}