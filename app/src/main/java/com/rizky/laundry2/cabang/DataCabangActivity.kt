package com.rizky.laundry2.cabang

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.tambahan.DataTambahanActivity

class DataCabangActivity : AppCompatActivity() {
    lateinit var rvDATA_CABANG : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)
        init()
        pencet()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rvDATA_CABANG=findViewById(R.id.rvDATA_CABANG)
    }
    fun pencet() {
        rvDATA_CABANG.setOnClickListener {
            val intent = Intent(this@DataCabangActivity, TambahCabangActivity::class.java)
            startActivity(intent)
        }
    }
}