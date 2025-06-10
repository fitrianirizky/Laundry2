package com.rizky.laundry2.tambahan

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
import com.rizky.laundry2.adapter.DataTambahanAdapter
import com.rizky.laundry2.modeldata.ModelTambahan

class DataTambahanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")
    lateinit var rvDATA_TAMBAHAN : RecyclerView
    lateinit var fbDATA_TAMBAHAN : FloatingActionButton
    lateinit var tambahanList: ArrayList<ModelTambahan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDATA_TAMBAHAN.layoutManager = layoutManager
        rvDATA_TAMBAHAN.setHasFixedSize(true)
        tambahanList= arrayListOf<ModelTambahan>()
        getData()
        pencet()
        fbDATA_TAMBAHAN.setOnClickListener {
            val intent = Intent(this, TambahTambahanActivity::class.java)
            intent.putExtra("judul", this.getString(R.string.tvTambahan_Tambahan))
            intent.putExtra("idTambahan", "")
            intent.putExtra(" namaTambahan", "")
            intent.putExtra("hargaTambahan", "")
            intent.putExtra("namaCabangLayananTambahan", "")
            intent.putExtra("statusLayananTambahan", "")
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rvDATA_TAMBAHAN=findViewById(R.id.rvDATA_TAMBAHAN)
        fbDATA_TAMBAHAN=findViewById(R.id.fbDATA_TAMBAHAN)
    }

    fun getData(){
        val query = myRef.orderByChild("idTambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    tambahanList.clear()
                    for (dataSnapshot in snapshot.children){
                        val tambahan= dataSnapshot.getValue(ModelTambahan::class.java)
                        tambahanList.add(tambahan!!)
                    }
                    val adapter = DataTambahanAdapter(tambahanList)
                    rvDATA_TAMBAHAN.adapter= adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataTambahanActivity, error.message, Toast.LENGTH_SHORT)
            }
        })
    }

    fun pencet(){
        fbDATA_TAMBAHAN.setOnClickListener{
            val intent = Intent(this@DataTambahanActivity, TambahTambahanActivity::class.java)
            startActivity(intent)
        }
    }


}