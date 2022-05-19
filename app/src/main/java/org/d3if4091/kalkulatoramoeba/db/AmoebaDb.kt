package org.d3if4091.kalkulatoramoeba.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AmoebaEntity::class], version = 1, exportSchema = false)
abstract class AmoebaDb : RoomDatabase() {
    abstract val dao: AmoebaDao
    companion object {
        @Volatile
        private var INSTANCE: AmoebaDb? = null

        fun getInstance(context: Context): AmoebaDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AmoebaDb::class.java,
                        "amoeba.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}