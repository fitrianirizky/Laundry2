package com.rizky.laundry2.tambahan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.rizky.laundry2.R
import com.rizky.laundry2.cabang.TambahCabangActivity

class DataTambahanActivity : AppCompatActivity() {
    lateinit var rvDATA_TAMBAHAN : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)
        init()
        pencet()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rvDATA_TAMBAHAN=findViewById(R.id.rvDATA_TAMBAHAN)
    }
    fun pencet() {
        rvDATA_TAMBAHAN.setOnClickListener {
            val intent = Intent(this@DataTambahanActivity, TambahTambahanActivity::class.java)
            startActivity(intent)
        }
    }
}