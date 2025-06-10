package com.rizky.laundry2.transaksi

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelPelanggan

class PilihPelangganActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")
    private lateinit var rvPILIH_PELANGGAN: RecyclerView
    private lateinit var pelangganList: ArrayList<ModelPelanggan>
    private lateinit var fullList: ArrayList<ModelPelanggan>
    private lateinit var tvKosong: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: PilihPelangganAdapter
    private var idCabang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_pelanggan)

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        idCabang = sharedPref.getString("idCabang", null).toString()

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPILIH_PELANGGAN.layoutManager = layoutManager
        rvPILIH_PELANGGAN.setHasFixedSize(true)

        pelangganList = arrayListOf()
        fullList = arrayListOf()

        adapter = PilihPelangganAdapter(pelangganList)
        rvPILIH_PELANGGAN.adapter = adapter

        getData()
        setupSearch()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        rvPILIH_PELANGGAN = findViewById(R.id.rvPILIH_PELANGGAN)
        tvKosong = findViewById(R.id.tvPILIH_PELANGGAN_Kosong)
        searchView = findViewById(R.id.svPILIH_PELANGGAN)
    }
    private fun getData() {
        val query = myRef.limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pelangganList.clear()
                fullList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        if (pelanggan != null) {
                            pelangganList.add(pelanggan)
                            fullList.add(pelanggan)
                        }
                    }
                    tvKosong.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                } else {
                    tvKosong.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error jika perlu
            }
        })
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        val filteredList = ArrayList<ModelPelanggan>()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(fullList)
        } else {
            val search = query.lowercase()
            for (item in fullList) {
                if (
                    item.namaPelanggan?.lowercase()?.contains(search) == true ||
                    item.noHPPelanggan?.lowercase()?.contains(search) == true ||
                    item.alamatPelanggan?.lowercase()?.contains(search) == true
                ) {
                    filteredList.add(item)
                }
            }
        }

        pelangganList.clear()
        pelangganList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }
}