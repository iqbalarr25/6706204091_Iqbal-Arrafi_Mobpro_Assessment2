package org.d3if4091.kalkulatoramoeba.model

import org.d3if4091.kalkulatoramoeba.db.AmoebaEntity

fun AmoebaEntity.hitungAmoeba(): HasilAmoeba{
    val jumlahWaktuPembelahan = jangkaWaktu.toDouble() /  rentangWaktu.toDouble()
    val hasil = awalAmoeba.toDouble() * (Math.pow(pembelahanAmoeba.toDouble(), jumlahWaktuPembelahan))
    return HasilAmoeba(hasil.toFloat(), awalAmoeba, pembelahanAmoeba, rentangWaktu, jangkaWaktu)
}