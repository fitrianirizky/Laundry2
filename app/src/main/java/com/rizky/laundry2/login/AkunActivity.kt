package com.rizky.laundry2.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rizky.laundry2.R

class AkunActivity : AppCompatActivity() {
    private lateinit var tvNamaUser: TextView
    private lateinit var tvNoHP: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvCabang: TextView
    private lateinit var btnLogout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_akun)

        init()
        getData()
        pencet()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvNamaUser = findViewById(R.id.tvNamaUser)
        tvNoHP = findViewById(R.id.tvNoHp)
        tvRole = findViewById(R.id.tvRole)
        tvCabang= findViewById(R.id.tvCabang)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun getData() {
        val prefs = getSharedPreferences("LOGIN_PREFS", Context.MODE_PRIVATE)
        tvNamaUser.text = prefs.getString("nama", "-")
        tvNoHP.text = prefs.getString("noHP", "-")
        tvRole.text = prefs.getString("role", "-")
        tvCabang.text = prefs.getString("cabang", "-")
    }

    private fun pencet() {
        btnLogout.setOnClickListener {
            getSharedPreferences("LOGIN_PREFS", Context.MODE_PRIVATE).edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}