package com.rizky.laundry2.layanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.cabang.TambahCabangActivity

class DataLayananActivity : AppCompatActivity() {
    lateinit var rvDATA_LAYANAN : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_layanan)
        init()
        pencet()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rvDATA_LAYANAN=findViewById(R.id.rvDATA_LAYANAN)
    }
    fun pencet() {
        rvDATA_LAYANAN.setOnClickListener {
            val intent = Intent(this@DataLayananActivity, TambahLayananActivity::class.java)
            startActivity(intent)
        }
    }
}