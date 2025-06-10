package com.rizky.laundry2.pelanggan

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
import com.rizky.laundry2.modeldata.ModelPelanggan

class TambahPelangganActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var tvTAMBAH_PELANGGAN: TextView
    lateinit var tvTAMBAH_NAMA_PELANGGAN : TextView
    lateinit var etTAMBAH_NAMA_PELANGGAN : EditText
    lateinit var tvTAMBAH_ALAMAT_PELANGGAN : TextView
    lateinit var etTAMBAH_ALAMAT_PELANGGAN : EditText
    lateinit var tvTAMBAH_NOHP_PELANGGAN : TextView
    lateinit var etTAMBAH_NOHP_PELANGGAN : EditText
    lateinit var tvTAMBAH_CABANG_PELANGGAN : TextView
    lateinit var etTAMBAH_CABANG_PELANGGAN : EditText
    lateinit var btSIMPAN : Button

    var idPelanggan: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)
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
        tvTAMBAH_PELANGGAN = findViewById(R.id.tvTAMBAH_PELANGGAN)
        tvTAMBAH_NAMA_PELANGGAN = findViewById(R.id.tvTAMBAH_NAMA_PELANGGAN)
        etTAMBAH_NAMA_PELANGGAN = findViewById(R.id.etTAMBAH_NAMA_PELANGGAN)
        tvTAMBAH_ALAMAT_PELANGGAN = findViewById(R.id.tvTAMBAH_ALAMAT_PELANGGAN)
        etTAMBAH_ALAMAT_PELANGGAN = findViewById(R.id.etTAMBAH_ALAMAT_PELANGGAN)
        tvTAMBAH_NOHP_PELANGGAN = findViewById(R.id.tvTAMBAH_NOHP_PELANGGAN)
        etTAMBAH_NOHP_PELANGGAN = findViewById(R.id.etTAMBAH_NOHP_PELANGGAN)
        tvTAMBAH_CABANG_PELANGGAN = findViewById(R.id.tvTAMBAH_CABANG_PELANGGAN)
        etTAMBAH_CABANG_PELANGGAN = findViewById(R.id.etTAMBAH_CABANG_PELANGGAN)
        btSIMPAN = findViewById(R.id.btSIMPAN)

    }

    fun getData(){
        idPelanggan = intent.getStringExtra("idPelanggan").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaPelanggan")
        val alamat = intent.getStringExtra("alamatPelanggan")
        val hp = intent.getStringExtra("noHPPelanggan")
        val cabang= intent.getStringExtra("idCabang")
        tvTAMBAH_PELANGGAN.text = judul
        etTAMBAH_NAMA_PELANGGAN.setText(nama)
        etTAMBAH_ALAMAT_PELANGGAN.setText(alamat)
        etTAMBAH_NOHP_PELANGGAN.setText(hp)
        etTAMBAH_CABANG_PELANGGAN.setText(cabang)
        if (!tvTAMBAH_PELANGGAN.text.equals(this.getString(R.string.tvTAMBAH_PELANGGAN))){
            if (judul.equals(getString(R.string.EditPelanggan))){
                mati()
                btSIMPAN.text=getString(R.string.Sunting)
            }
        }else{
            hidup()
            etTAMBAH_NAMA_PELANGGAN.requestFocus()
            btSIMPAN.text=getString(R.string.simpan)
        }
    }

    fun mati(){
        etTAMBAH_NAMA_PELANGGAN.isEnabled=false
        etTAMBAH_ALAMAT_PELANGGAN.isEnabled=false
        etTAMBAH_NOHP_PELANGGAN.isEnabled=false
        etTAMBAH_CABANG_PELANGGAN.isEnabled=false
    }

    fun hidup(){
        etTAMBAH_NAMA_PELANGGAN.isEnabled=true
        etTAMBAH_ALAMAT_PELANGGAN.isEnabled=true
        etTAMBAH_NOHP_PELANGGAN.isEnabled=true
        etTAMBAH_CABANG_PELANGGAN.isEnabled=true
    }

    fun cekValidasi(){
        val nama = etTAMBAH_NAMA_PELANGGAN.text.toString()
        val alamat = etTAMBAH_ALAMAT_PELANGGAN.text.toString()
        val noHP = etTAMBAH_NOHP_PELANGGAN.text.toString()
        val cabang = etTAMBAH_CABANG_PELANGGAN.text.toString()

        //validasi data
        if (nama.isEmpty()){
            etTAMBAH_NAMA_PELANGGAN.error=this.getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_NAMA_PELANGGAN.requestFocus()
            return
        }

        if (alamat.isEmpty()){
            etTAMBAH_ALAMAT_PELANGGAN.error=this.getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_ALAMAT_PELANGGAN.requestFocus()
            return
        }
        if (noHP.isEmpty()){
            etTAMBAH_NOHP_PELANGGAN.error=this.getString(R.string.validasi_noHP_pelanggan)
            Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.validasi_noHP_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_NOHP_PELANGGAN.requestFocus()
            return
        }
        if (cabang.isEmpty()){
            etTAMBAH_CABANG_PELANGGAN.error=this.getString(R.string.validasi_cabang_pelanggan)
            Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.validasi_cabang_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_CABANG_PELANGGAN.requestFocus()
            return
        }
        if (btSIMPAN.text.equals(getString(R.string.simpan))){
            simpan()
        }else if (btSIMPAN.text.equals(getString(R.string.Sunting))){
            hidup()
            etTAMBAH_NAMA_PELANGGAN.requestFocus()
            btSIMPAN.text=getString(R.string.perbarui)
        }else if (btSIMPAN.text.equals(getString(R.string.perbarui))) {
            update()
        }
    }

    fun update(){
        val pelangganRef = database.getReference("pelanggan").child(idPelanggan)
        val data =ModelPelanggan(
            idPelanggan,
            etTAMBAH_NAMA_PELANGGAN.text.toString(),
            etTAMBAH_ALAMAT_PELANGGAN.text.toString(),
            etTAMBAH_NOHP_PELANGGAN.text.toString(),
            etTAMBAH_CABANG_PELANGGAN.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        //buat map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPelanggan"] = data.namaPelanggan.toString()
        updateData["alamatPelanggan"] = data.alamatPelanggan.toString()
        updateData["noHPPelanggan"] = data.noHPPelanggan.toString()
        updateData["idCabang"] = data.idCabang.toString()
        pelangganRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahPelangganActivity, getString(R.string.sukses_update_pelanggan), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@TambahPelangganActivity, getString(R.string.gagal_update_pelanggan), Toast.LENGTH_SHORT).show()
        }
    }

    fun simpan(){
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key

        val data = ModelPelanggan(
            pelangganId ?: "",
            etTAMBAH_NAMA_PELANGGAN.text.toString(),
            etTAMBAH_ALAMAT_PELANGGAN.text.toString(),
            etTAMBAH_NOHP_PELANGGAN.text.toString(),
            etTAMBAH_CABANG_PELANGGAN.text.toString(),
            timestamp = System.currentTimeMillis()


        )
        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@TambahPelangganActivity, this.getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()
            }
    }

}