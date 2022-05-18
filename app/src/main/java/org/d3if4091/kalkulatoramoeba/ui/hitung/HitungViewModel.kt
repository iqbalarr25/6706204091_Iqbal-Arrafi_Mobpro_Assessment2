package org.d3if4091.kalkulatoramoeba.ui.hitung

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if4091.kalkulatoramoeba.model.HasilAmoeba

class HitungViewModel: ViewModel() {

    private val hasilAmoeba = MutableLiveData<HasilAmoeba?>()

    fun hitungAmoeba(awalAmoeba : Float, pembelahanAmoeba: Float, rentangWaktu: Float, jangkaWaktu: Float){
        //perhitungan aritmatika pembelahan amoeba
        val jumlahWaktuPembelahan = jangkaWaktu.toDouble() /  rentangWaktu.toDouble()
        val hasil = awalAmoeba.toDouble() * (Math.pow(pembelahanAmoeba.toDouble(), jumlahWaktuPembelahan))

        hasilAmoeba.value = HasilAmoeba(hasil.toFloat(), awalAmoeba, pembelahanAmoeba, rentangWaktu, jangkaWaktu)
    }
    fun getHasilAmoeba(): LiveData<HasilAmoeba?> = hasilAmoeba

    fun deleteHasilAmoeba(){
        hasilAmoeba.value = null
    }
}