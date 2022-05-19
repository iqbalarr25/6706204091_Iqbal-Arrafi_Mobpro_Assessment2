package org.d3if4091.kalkulatoramoeba.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "amoeba")
data class AmoebaEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var awalAmoeba: Float,
    var pembelahanAmoeba: Float,
    var rentangWaktu: Float,
    var jangkaWaktu: Float
)