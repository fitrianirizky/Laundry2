package com.rizky.laundry2.modeldata

data class ModelNotaTransaksi(
    val idTransaksi: String? = null,
    val cabang: String? = null,
    val tanggal: String? = null,
    val namaPelanggan: String? = null,
    val namaKaryawan: String? = null,
    val layanan: String? = null,
    val hargaLayanan: String? = null,
    val tambahan: List<ModelTransaksiTambahan>? = null,
    val totalBayar: String? = null,
    val metodePembayaran: String? = null
)