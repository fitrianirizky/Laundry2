package com.rizky.laundry2.laporan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.rizky.laundry2.R
import com.rizky.laundry2.adapter.LaporanAdapter
import com.rizky.laundry2.modeldata.ModelLaporan

class DataLaporanActivity : AppCompatActivity() {

    private lateinit var rvLaporan: RecyclerView
    private lateinit var laporanAdapter: LaporanAdapter
    private lateinit var databaseReference: DatabaseReference

    private val laporanList = ArrayList<ModelLaporan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_laporan)

        initViews()
        setupRecyclerView()
        setupWindowInsets()
        loadData()
    }

    private fun initViews() {
        rvLaporan = findViewById(R.id.rvDATA_LAPORAN)
    }

    private fun setupRecyclerView() {
        laporanAdapter = LaporanAdapter(laporanList, this)

        rvLaporan.apply {
            layoutManager = LinearLayoutManager(this@DataLaporanActivity).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            adapter = laporanAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("laporan")
        databaseReference.orderByChild("timestamp").limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    laporanList.clear()

                    if (!snapshot.exists()) {
                        laporanAdapter.notifyDataSetChanged()
                        return
                    }

                    for (data in snapshot.children) {
                        try {
                            val laporan = data.getValue(ModelLaporan::class.java)
                            laporan?.let {
                                it.idLaporan = data.key
                                laporanList.add(it)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    laporanAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DataLaporanActivity,
                        "Gagal memuat data: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
