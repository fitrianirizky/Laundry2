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
import com.rizky.laundry2.modeldata.ModelTransaksiTambahan

class PilihLayananTambahanActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")
    private lateinit var rvPILIH_TAMBAHAN: RecyclerView
    private lateinit var tambahanList: ArrayList<ModelTransaksiTambahan>
    private lateinit var fullList: ArrayList<ModelTransaksiTambahan>
    private lateinit var tvKosong: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: TambahanAdapter
    private var idCabang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_layanan_tambahan)

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        idCabang = sharedPref.getString("idCabang", null).toString()

        init()

        tambahanList = arrayListOf()
        fullList = arrayListOf()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPILIH_TAMBAHAN.layoutManager = layoutManager
        rvPILIH_TAMBAHAN.setHasFixedSize(true)

        adapter = TambahanAdapter(tambahanList)

        rvPILIH_TAMBAHAN.adapter = adapter


        getData()
        setupSearch()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        rvPILIH_TAMBAHAN = findViewById(R.id.rvPILIH_LAYANAN_TAMBAHAN)
        tvKosong = findViewById(R.id.tvPILIH_LAYANAN_TAMBAHAN_Kosong)
        searchView = findViewById(R.id.svPILIH_LAYANAN_TAMBAHAN)
    }
    private fun getData() {
        val query = myRef.limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tambahanList.clear()
                fullList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val tambahan = dataSnapshot.getValue(ModelTransaksiTambahan::class.java)
                        if (tambahan != null) {
                            tambahanList.add(tambahan)
                            fullList.add(tambahan)
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
        val filteredList = ArrayList<ModelTransaksiTambahan>()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(fullList)
        } else {
            val search = query.lowercase()
            for (item in fullList) {
                if (
                    item.namaTambahan?.lowercase()?.contains(search) == true ||
                    item.hargaTambahan?.lowercase()?.contains(search) == true
                ) {
                    filteredList.add(item)
                }
            }
        }

        tambahanList.clear()
        tambahanList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }
}