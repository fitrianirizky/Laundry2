package com.rizky.laundry2.modeldata

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ModelTransaksiTambahan(
    val idTambahan: String? = null,
    val namaTambahan: String? = null,
    val hargaTambahan: String? = null,
) : Serializable

