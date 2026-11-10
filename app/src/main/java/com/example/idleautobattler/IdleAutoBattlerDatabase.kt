package com.example.idleautobattler.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Progresso::class],
    version = 1,
    exportSchema = false,
)
abstract class IdleAutoBattlerDatabase : RoomDatabase() {
    abstract fun progressoDao(): ProgressoDao

    companion object {
        @Volatile
        private var instancia: IdleAutoBattlerDatabase? = null

        fun obterInstancia(context: Context): IdleAutoBattlerDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    IdleAutoBattlerDatabase::class.java,
                    "idle_auto_battler.db",
                ).build().also { instancia = it }
            }
        }
    }
}
