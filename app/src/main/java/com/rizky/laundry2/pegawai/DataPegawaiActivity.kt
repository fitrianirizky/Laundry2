package com.rizky.laundry2.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rizky.laundry2.R
import com.rizky.laundry2.cabang.TambahCabangActivity

class DataPegawaiActivity : AppCompatActivity() {
    lateinit var fbDATA_PEGAWAI : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)
        init()
        pencet()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        fbDATA_PEGAWAI=findViewById(R.id.fbDATA_PEGAWAI)
    }

    fun pencet() {
        fbDATA_PEGAWAI.setOnClickListener {
            val intent = Intent(this@DataPegawaiActivity, TambahPegawaiActivity::class.java)
            startActivity(intent)
        }
    }


}