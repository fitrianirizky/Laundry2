package com.rizky.laundry2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rizky.laundry2.cabang.DataCabangActivity
import com.rizky.laundry2.laporan.DataLaporanActivity
import com.rizky.laundry2.layanan.DataLayananActivity
import com.rizky.laundry2.login.AkunActivity
import com.rizky.laundry2.pegawai.DataPegawaiActivity
import com.rizky.laundry2.pelanggan.DataPelangganActivity
import com.rizky.laundry2.tambahan.DataTambahanActivity
import com.rizky.laundry2.transaksi.TransaksiActivity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BerandaActivity : AppCompatActivity() {

    private lateinit var tvSapa: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var cvPelanggan: CardView
    private lateinit var cvPegawai: CardView
    private lateinit var cvLayanan: CardView
    private lateinit var cvCabang: CardView
    private lateinit var cvTambahan: CardView
    private lateinit var cvTransaksi: CardView
    private lateinit var cvLaporan: CardView
    private lateinit var cvAkun: CardView

    private lateinit var role: String
    private var namaUser: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beranda)

        init()
        getRoleFromPrefs()
        getNameFromPrefs()
        applyRoleAccess()
        setListeners()

        tvSapa.text = getGreetingWithName()
        tvTanggal.text = getCurrentDate()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvSapa = findViewById(R.id.tvSapa)
        tvTanggal = findViewById(R.id.tvTanggal)
        cvPegawai = findViewById(R.id.cvPegawai)
        cvPelanggan = findViewById(R.id.cvPelanggan)
        cvLayanan = findViewById(R.id.cvLayanan)
        cvCabang = findViewById(R.id.cvCabang)
        cvTambahan = findViewById(R.id.cvTambahan)
        cvTransaksi = findViewById(R.id.cvTransaksi)
        cvLaporan = findViewById(R.id.cvLaporan)
        cvAkun = findViewById(R.id.cvAkun)
    }

    private fun getRoleFromPrefs() {
        val prefs = getSharedPreferences("LOGIN_PREFS", MODE_PRIVATE)
        role = prefs.getString("role", "user") ?: "user"
    }

    private fun applyRoleAccess() {
        when (role.lowercase()) {
            "admin" -> {
                cvPegawai.visibility = View.VISIBLE
                cvCabang.visibility = View.VISIBLE
                cvPelanggan.visibility = View.VISIBLE
                cvLayanan.visibility = View.VISIBLE
                cvTambahan.visibility = View.VISIBLE
                cvTransaksi.visibility = View.VISIBLE
                cvLaporan.visibility = View.VISIBLE
                cvAkun.visibility = View.VISIBLE
            }
            "pegawai" -> {
                cvPegawai.visibility = View.GONE
                cvCabang.visibility = View.GONE
                cvPelanggan.visibility = View.VISIBLE
                cvLayanan.visibility = View.VISIBLE
                cvTambahan.visibility = View.VISIBLE
                cvTransaksi.visibility = View.VISIBLE
                cvLaporan.visibility = View.VISIBLE
                cvAkun.visibility = View.VISIBLE
            }
            else -> {
                cvPegawai.visibility = View.GONE
                cvCabang.visibility = View.GONE
                cvPelanggan.visibility = View.GONE
                cvLayanan.visibility = View.GONE
                cvTambahan.visibility = View.GONE
                cvTransaksi.visibility = View.GONE
                cvLaporan.visibility = View.GONE
                cvAkun.visibility = View.GONE
                Toast.makeText(this, "Role tidak dikenal, silakan login ulang.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setListeners() {
        cvPegawai.setOnClickListener {
            startActivity(Intent(this, DataPegawaiActivity::class.java))
        }
        cvPelanggan.setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
        cvLayanan.setOnClickListener {
            startActivity(Intent(this, DataLayananActivity::class.java))
        }
        cvCabang.setOnClickListener {
            startActivity(Intent(this, DataCabangActivity::class.java))
        }
        cvTambahan.setOnClickListener {
            startActivity(Intent(this, DataTambahanActivity::class.java))
        }
        cvTransaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiActivity::class.java))
        }
        cvLaporan.setOnClickListener {
            startActivity(Intent(this, DataLaporanActivity::class.java))
        }

        cvAkun.setOnClickListener {
            startActivity(Intent(this, AkunActivity::class.java))
        }
    }

    private fun getGreeting(): String {
        val currentHour = LocalTime.now().hour
        return when (currentHour) {
            in 5..11 -> getString(R.string.SelamatPagi)
            in 12..15 -> getString(R.string.Selamatsiang)
            in 16..18 -> getString(R.string.SelamatSore)
            else -> getString(R.string.SelamatMalam)
        }
    }

    private fun getNameFromPrefs() {
        val prefs = getSharedPreferences("LOGIN_PREFS", MODE_PRIVATE)
        namaUser = prefs.getString("nama", "User") ?: "User"
    }

    private fun getGreetingWithName(): String {
        val cleanName = namaUser.trim() // pastikan gak ada spasi atau koma
        val displayName = if (cleanName.isBlank() || cleanName.equals("user", ignoreCase = true)) "" else ", $cleanName"
        return "${getGreeting()}$displayName"
    }

    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return currentDate.format(formatter)
    }
}
