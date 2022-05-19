package org.d3if4091.kalkulatoramoeba.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AmoebaDao {

    @Insert
    fun insert(amoeba: AmoebaEntity)

    @Query("SELECT * FROM amoeba ORDER BY id")
    fun getLastBmi(): LiveData<List<AmoebaEntity>>

    @Delete
    fun clearData(amoeba: AmoebaEntity)
}