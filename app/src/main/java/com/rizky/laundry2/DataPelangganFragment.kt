package com.rizky.laundry2.pelanggan

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.rizky.laundry2.R
import com.rizky.laundry2.adapter.DataPelangganAdapter
import com.rizky.laundry2.modeldata.ModelPelanggan

class DataPelangganFragment : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    private lateinit var rvDATA_PELANGGAN: RecyclerView
    private lateinit var fbDATA_PELANGGAN: FloatingActionButton
    private lateinit var pelangganList: ArrayList<ModelPelanggan>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_pelanggan, container, false)
        init(view)

        val layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        rvDATA_PELANGGAN.layoutManager = layoutManager
        rvDATA_PELANGGAN.setHasFixedSize(true)

        pelangganList = arrayListOf()
        getData()

        fbDATA_PELANGGAN.setOnClickListener {
            val tambahFragment = TambahPelangganFragment()
            val bundle = Bundle()
            bundle.putString("judul", getString(R.string.tvTAMBAH_PELANGGAN))
            bundle.putString("idPelanggan", "")
            bundle.putString("namaPelanggan", "")
            bundle.putString("alamatPelanggan", "")
            bundle.putString("noHPPelanggan", "")
            bundle.putString("idCabang", "")
            tambahFragment.arguments = bundle

            val containerId = if (requireActivity().findViewById<View?>(R.id.fragment_tambah_pelanggan) != null) {
                R.id.fragment_tambah_pelanggan
            } else {
                R.id.fragment_container
            }

            parentFragmentManager.beginTransaction()
                .replace(containerId, tambahFragment)
                .addToBackStack(null)
                .commit()
        }


        return view
    }

    private fun init(view: View) {
        rvDATA_PELANGGAN = view.findViewById(R.id.rvDATA_PELANGGAN)
        fbDATA_PELANGGAN = view.findViewById(R.id.fbDATA_PELANGGAN)
    }

    private fun getData() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        pelanggan?.let { pelangganList.add(it) }
                    }
                    val adapter = DataPelangganAdapter(pelangganList)
                    rvDATA_PELANGGAN.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
