package org.d3if4091.kalkulatoramoeba.ui.histori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if4091.kalkulatoramoeba.db.AmoebaDao
import org.d3if4091.kalkulatoramoeba.db.AmoebaEntity

class HistoriViewModel(private val db: AmoebaDao) : ViewModel() {
    val data = db.getLastBmi()

    fun hapusData(amoeba: AmoebaEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.clearData(amoeba)
        }
    }
}