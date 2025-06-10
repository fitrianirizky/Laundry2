package com.rizky.laundry2.pelanggan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.rizky.laundry2.R
import com.rizky.laundry2.modeldata.ModelPelanggan

class TambahPelangganFragment : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

    lateinit var tvTAMBAH_PELANGGAN: TextView
    lateinit var tvTAMBAH_NAMA_PELANGGAN: TextView
    lateinit var etTAMBAH_NAMA_PELANGGAN: EditText
    lateinit var tvTAMBAH_ALAMAT_PELANGGAN: TextView
    lateinit var etTAMBAH_ALAMAT_PELANGGAN: EditText
    lateinit var tvTAMBAH_NOHP_PELANGGAN: TextView
    lateinit var etTAMBAH_NOHP_PELANGGAN: EditText
    lateinit var tvTAMBAH_CABANG_PELANGGAN: TextView
    lateinit var etTAMBAH_CABANG_PELANGGAN: EditText
    lateinit var btSIMPAN: Button

    var idPelanggan: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah_pelanggan, container, false)
        init(view)
        getData()
        btSIMPAN.setOnClickListener {
            cekValidasi()
        }
        return view
    }

    private fun init(view: View) {
        tvTAMBAH_PELANGGAN = view.findViewById(R.id.tvTAMBAH_PELANGGAN)
        tvTAMBAH_NAMA_PELANGGAN = view.findViewById(R.id.tvTAMBAH_NAMA_PELANGGAN)
        etTAMBAH_NAMA_PELANGGAN = view.findViewById(R.id.etTAMBAH_NAMA_PELANGGAN)
        tvTAMBAH_ALAMAT_PELANGGAN = view.findViewById(R.id.tvTAMBAH_ALAMAT_PELANGGAN)
        etTAMBAH_ALAMAT_PELANGGAN = view.findViewById(R.id.etTAMBAH_ALAMAT_PELANGGAN)
        tvTAMBAH_NOHP_PELANGGAN = view.findViewById(R.id.tvTAMBAH_NOHP_PELANGGAN)
        etTAMBAH_NOHP_PELANGGAN = view.findViewById(R.id.etTAMBAH_NOHP_PELANGGAN)
        tvTAMBAH_CABANG_PELANGGAN = view.findViewById(R.id.tvTAMBAH_CABANG_PELANGGAN)
        etTAMBAH_CABANG_PELANGGAN = view.findViewById(R.id.etTAMBAH_CABANG_PELANGGAN)
        btSIMPAN = view.findViewById(R.id.btSIMPAN)
    }

    private fun getData() {
        val tambahFragment = TambahPelangganFragment()
        val bundle = arguments

        idPelanggan = bundle?.getString("idPelanggan").orEmpty()
        val judul = bundle?.getString("judul")
        val nama = bundle?.getString("namaPelanggan")
        val alamat = bundle?.getString("alamatPelanggan")
        val hp = bundle?.getString("noHPPelanggan")
        val cabang = bundle?.getString("idCabang")

        val parentActivity = requireActivity() as DataPelangganActivity
        val isLandscape = parentActivity.findViewById<View?>(R.id.fragment_tambah_pelanggan) != null


        if (isLandscape) {
            // Replace fragment di kanan langsung (landscape)
            parentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_tambah_pelanggan, tambahFragment)
                .addToBackStack(null)
                .commit()
        } else {
            // Portrait mode: pindah ke TambahPelangganActivity baru
            val intent = Intent(requireContext(), TambahPelangganActivity::class.java)
            // bawa data lewat intent jika perlu
            startActivity(intent)
        }

        tvTAMBAH_PELANGGAN.text = judul
        etTAMBAH_NAMA_PELANGGAN.setText(nama)
        etTAMBAH_ALAMAT_PELANGGAN.setText(alamat)
        etTAMBAH_NOHP_PELANGGAN.setText(hp)
        etTAMBAH_CABANG_PELANGGAN.setText(cabang)

        if (judul != getString(R.string.tvTAMBAH_PELANGGAN)) {
            if (judul == getString(R.string.EditPelanggan)) {
                mati()
                btSIMPAN.text = getString(R.string.Sunting)
            }
        } else {
            hidup()
            etTAMBAH_NAMA_PELANGGAN.requestFocus()
            btSIMPAN.text = getString(R.string.simpan)
        }
    }

    private fun mati() {
        etTAMBAH_NAMA_PELANGGAN.isEnabled = false
        etTAMBAH_ALAMAT_PELANGGAN.isEnabled = false
        etTAMBAH_NOHP_PELANGGAN.isEnabled = false
        etTAMBAH_CABANG_PELANGGAN.isEnabled = false
    }

    private fun hidup() {
        etTAMBAH_NAMA_PELANGGAN.isEnabled = true
        etTAMBAH_ALAMAT_PELANGGAN.isEnabled = true
        etTAMBAH_NOHP_PELANGGAN.isEnabled = true
        etTAMBAH_CABANG_PELANGGAN.isEnabled = true
    }

    private fun cekValidasi() {
        val nama = etTAMBAH_NAMA_PELANGGAN.text.toString()
        val alamat = etTAMBAH_ALAMAT_PELANGGAN.text.toString()
        val noHP = etTAMBAH_NOHP_PELANGGAN.text.toString()
        val cabang = etTAMBAH_CABANG_PELANGGAN.text.toString()

        if (nama.isEmpty()) {
            etTAMBAH_NAMA_PELANGGAN.error = getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(requireContext(), getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_NAMA_PELANGGAN.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etTAMBAH_ALAMAT_PELANGGAN.error = getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(requireContext(), getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_ALAMAT_PELANGGAN.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etTAMBAH_NOHP_PELANGGAN.error = getString(R.string.validasi_noHP_pelanggan)
            Toast.makeText(requireContext(), getString(R.string.validasi_noHP_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_NOHP_PELANGGAN.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etTAMBAH_CABANG_PELANGGAN.error = getString(R.string.validasi_cabang_pelanggan)
            Toast.makeText(requireContext(), getString(R.string.validasi_cabang_pelanggan), Toast.LENGTH_SHORT).show()
            etTAMBAH_CABANG_PELANGGAN.requestFocus()
            return
        }

        when (btSIMPAN.text) {
            getString(R.string.simpan) -> simpan()
            getString(R.string.Sunting) -> {
                hidup()
                etTAMBAH_NAMA_PELANGGAN.requestFocus()
                btSIMPAN.text = getString(R.string.perbarui)
            }
            getString(R.string.perbarui) -> update()
        }
    }

    private fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key ?: ""

        val data = ModelPelanggan(
            pelangganId,
            etTAMBAH_NAMA_PELANGGAN.text.toString(),
            etTAMBAH_ALAMAT_PELANGGAN.text.toString(),
            etTAMBAH_NOHP_PELANGGAN.text.toString(),
            etTAMBAH_CABANG_PELANGGAN.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), getString(R.string.gagal_simpan_pelanggan), Toast.LENGTH_SHORT).show()
            }
    }

    private fun update() {
        val pelangganRef = database.getReference("pelanggan").child(idPelanggan)
        val data = ModelPelanggan(
            idPelanggan,
            etTAMBAH_NAMA_PELANGGAN.text.toString(),
            etTAMBAH_ALAMAT_PELANGGAN.text.toString(),
            etTAMBAH_NOHP_PELANGGAN.text.toString(),
            etTAMBAH_CABANG_PELANGGAN.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        val updateData = mutableMapOf<String, Any>()
        updateData["namaPelanggan"] = data.namaPelanggan ?: ""
        updateData["alamatPelanggan"] = data.alamatPelanggan ?: ""
        updateData["noHPPelanggan"] = data.noHPPelanggan ?: ""
        updateData["idCabang"] = data.idCabang ?: ""

        pelangganRef.updateChildren(updateData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), getString(R.string.sukses_update_pelanggan), Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), getString(R.string.gagal_update_pelanggan), Toast.LENGTH_SHORT).show()
            }
    }
}
