package com.rizky.laundry2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rizky.laundry2.pelanggan.DataPelangganActivity
import com.rizky.laundry2.pelanggan.DataPelangganFragment
import com.rizky.laundry2.pelanggan.TambahPelangganFragment

class PelangganActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan)

        val fragmentManager = supportFragmentManager

        if (findViewById<View?>(R.id.fragment_data_pelanggan) != null &&
            findViewById<View?>(R.id.fragment_tambah_pelanggan) != null) {
            // Ini layout landscape, tampilkan 2 fragment
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_data_pelanggan, DataPelangganFragment())
                .replace(R.id.fragment_tambah_pelanggan, TambahPelangganFragment())
                .commit()
        } else {
            // Ini layout potrait, tampilkan DataPelanggan saja
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DataPelangganFragment())
                .commit()
        }
    }
}
