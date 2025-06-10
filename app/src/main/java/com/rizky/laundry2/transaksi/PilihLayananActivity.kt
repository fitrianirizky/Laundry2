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
import com.rizky.laundry2.modeldata.ModelLayanan

class PilihLayananActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")
    private lateinit var rvPILIH_LAYANAN: RecyclerView
    private lateinit var layananList: ArrayList<ModelLayanan>
    private lateinit var fullList: ArrayList<ModelLayanan>
    private lateinit var tvKosong: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: PilihLayananAdapter
    private var idCabang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_layanan)

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        idCabang = sharedPref.getString("idCabang", null).toString()

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPILIH_LAYANAN.layoutManager = layoutManager
        rvPILIH_LAYANAN.setHasFixedSize(true)

        layananList = arrayListOf()
        fullList = arrayListOf()

        adapter = PilihLayananAdapter(layananList)
        rvPILIH_LAYANAN.adapter = adapter

        getData()
        setupSearch()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        rvPILIH_LAYANAN = findViewById(R.id.rvPILIH_LAYANAN)
        tvKosong = findViewById(R.id.tvPILIH_LAYANAN_Kosong)
        searchView = findViewById(R.id.svPILIH_LAYANAN)
    }
    private fun getData() {
        val query = myRef.limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                layananList.clear()
                fullList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(ModelLayanan::class.java)
                        if (layanan != null) {
                            layananList.add(layanan)
                            fullList.add(layanan)
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
        val filteredList = ArrayList<ModelLayanan>()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(fullList)
        } else {
            val search = query.lowercase()
            for (item in fullList) {
                if (
                    item.namaLayanan?.lowercase()?.contains(search) == true ||
                    item.hargaLayanan?.lowercase()?.contains(search) == true
                ) {
                    filteredList.add(item)
                }
            }
        }

        layananList.clear()
        layananList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }
}