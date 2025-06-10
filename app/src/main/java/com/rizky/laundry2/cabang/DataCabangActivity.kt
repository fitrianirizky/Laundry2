package com.rizky.laundry2.cabang

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rizky.laundry2.R
import com.rizky.laundry2.adapter.DataCabangAdapter
import com.rizky.laundry2.modeldata.ModelCabang
import com.rizky.laundry2.pegawai.TambahPegawaiActivity

class DataCabangActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var rvDATA_CABANG : RecyclerView
    lateinit var fbDATA_CABANG : FloatingActionButton
    lateinit var cabangList : ArrayList<ModelCabang>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDATA_CABANG.layoutManager = layoutManager
        rvDATA_CABANG.setHasFixedSize(true)
        cabangList = arrayListOf<ModelCabang>()
        getData()
        pencet()
        fbDATA_CABANG.setOnClickListener {
            val intent = Intent(this, TambahCabangActivity::class.java)
            intent.putExtra("judul", this.getString(R.string.tvTambah_Cabang))
            intent.putExtra("idCabang", "")
            intent.putExtra(" namaCabang", "")
            intent.putExtra("alamatCabang", "")
            intent.putExtra("noHPCabang", "")
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rvDATA_CABANG=findViewById(R.id.rvDATA_CABANG)
        fbDATA_CABANG=findViewById(R.id.fbDATA_CABANG)
    }

    fun getData(){
        val query = myRef.orderByChild("idCabang").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    cabangList.clear()
                    for (dataSnapshot in snapshot.children){
                        val cabang = dataSnapshot.getValue(ModelCabang::class.java)
                        cabangList.add(cabang!!)
                    }
                    val adapter = DataCabangAdapter(cabangList)
                    rvDATA_CABANG.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataCabangActivity, error.message, Toast.LENGTH_SHORT)
            }
        })
    }

    fun pencet(){
        fbDATA_CABANG.setOnClickListener{
            val intent = Intent(this@DataCabangActivity, TambahCabangActivity::class.java)
            startActivity(intent)
        }
    }

}