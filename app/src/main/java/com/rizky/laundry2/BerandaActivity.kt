package com.rizky.laundry2

import android.content.Intent
import android.os.Bundle
import android.util.Printer
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rizky.laundry2.cabang.DataCabangActivity
import com.rizky.laundry2.layanan.DataLayananActivity
import com.rizky.laundry2.pegawai.DataPegawaiActivity
import com.rizky.laundry2.pelanggan.DataPelangganActivity
import com.rizky.laundry2.tambahan.DataTambahanActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BerandaActivity : AppCompatActivity() {
    lateinit var tvSapa : TextView
    lateinit var tvTanggal : TextView
    lateinit var cvTambahan : CardView
    lateinit var cvLayanan : CardView
    lateinit var cvAkun : CardView
    lateinit var cvPegawai : CardView
    lateinit var cvCabang : CardView
    lateinit var cvPrinter : CardView
    lateinit var cvTransaksi : CardView
    lateinit var cvPelanggan : CardView
    lateinit var cvLaporan : CardView
    lateinit var ivTransaksi : ImageView
    lateinit var ivPelanggan : ImageView
    lateinit var ivLaporan : ImageView
    lateinit var ivAkun : ImageView
    lateinit var ivCabang : ImageView
    lateinit var ivPegawai : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beranda)
        init()
        pencet()
        tvSapa.text = getGreeting()
        tvTanggal.text = getCurrentDate()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        tvSapa=findViewById(R.id.tvSapa)
        tvTanggal=findViewById(R.id.tvTanggal)
        cvTambahan=findViewById(R.id.cvTambahan)
        cvLayanan=findViewById(R.id.cvLayanan)
    }

    fun pencet(){
        cvTambahan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataTambahanActivity::class.java)
            startActivity(intent)
        }
        cvLayanan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        cvAkun.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        cvCabang.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataCabangActivity::class.java)
            startActivity(intent)
        }
        cvPelanggan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        cvPegawai.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataPegawaiActivity::class.java)
            startActivity(intent)
        }
        cvPrinter.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        cvTransaksi.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        cvLaporan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        ivAkun.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        ivCabang.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataCabangActivity::class.java)
            startActivity(intent)
        }
        ivPelanggan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        ivPegawai.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataPegawaiActivity::class.java)
            startActivity(intent)
        }
        ivTransaksi.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }
        ivLaporan.setOnClickListener {
            val intent = Intent(this@BerandaActivity, DataLayananActivity::class.java)
            startActivity(intent)
        }

    }

    fun getGreeting(): String{
        val currentHour = java.time.LocalTime.now().hour
        return when (currentHour) {
            in 5..11-> "Selamat Pagi, Rizky"
            in 12..15-> "Selamat Siang, Rizky"
            in 16..18-> "Selamat Sore, Rizky"
            else -> "Selamat Malam, Rizky"
        }
    }

    fun getCurrentDate(): String {
        // Ambil tanggal saat ini
        val currentDate = LocalDate.now()

        // Format tanggal sesuai rl
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return currentDate.format(formatter)
    }

    fun main() {
        val tvGreeting = findViewById<TextView>(R.id.tvSapa)
        tvGreeting.text = getGreeting()

        val tvDate = findViewById<TextView>(R.id.tvTanggal)
        tvDate.text = getCurrentDate()
    }
}