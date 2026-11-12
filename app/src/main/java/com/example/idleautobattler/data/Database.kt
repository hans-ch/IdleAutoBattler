package com.example.idleautobattler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.idleautobattler.telas.combate.ProgressoDao
import com.example.idleautobattler.telas.combate.ProgressoEntity
import com.example.idleautobattler.telas.loja.ItemLojaDao
import com.example.idleautobattler.telas.loja.ItemLojaEntity
import com.example.idleautobattler.telas.perfil.SkinDao
import com.example.idleautobattler.telas.perfil.SkinEntity

@Database(
    entities = [
        ProgressoEntity::class,
        ItemLojaEntity::class,
        SkinEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressoDao(): ProgressoDao
    abstract fun itemLojaDao(): ItemLojaDao
    abstract fun skinDao(): SkinDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "idle_battler_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
