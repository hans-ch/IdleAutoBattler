package com.example.idleautobattler.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.idleautobattler.telas.combate.ProgressoDao
import com.example.idleautobattler.telas.combate.ProgressoEntity
import com.example.idleautobattler.telas.loja.ItemLojaDao
import com.example.idleautobattler.telas.loja.ItemLojaEntity
import com.example.idleautobattler.telas.perfil.SkinDao
import com.example.idleautobattler.telas.perfil.SkinEntity

@Database(
    entities = [ProgressoEntity::class, ItemLojaEntity::class, SkinEntity::class],
    version = 1,
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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "idle_battler_database.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
