package com.rizky.laundry2.modeldata

class ModelTransaksi (
    val idPelanggan: String,
    val namaPelanggan: String,
    val noHP: String,
    val idLayanan: String,
    val namaLayanan: String,
    val hargaLayanan: String,
    val idPegawai: String,
    val idCabang: String,
    val dataTambahan: List<ModelTransaksiTambahan>
)