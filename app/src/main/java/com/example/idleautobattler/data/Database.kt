package com.example.idleautobattler.data

import android.content.Context
import androidx.room3.*
import kotlinx.coroutines.flow.Flow

// --- ENTIDADES DO ROOM ---
@Entity(tableName = "progresso")
data class ProgressoEntity(
    @PrimaryKey val id: Int = 1,
    val ouro: Int = 1250,
    val dano: Int = 15,
    val vidaAtual: Int = 100,
    val vidaMaxima: Int = 100,
    val nivelMonstro: Int = 1,
    val skinEquipada: String = "Herói Padrão"
)

@Entity(tableName = "itens_loja")
data class ItemLojaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val nivelAtual: Int,
    val precoAtual: Int,
    val tipo: String,
    val aumentoDano: Int = 5
)

@Entity(tableName = "skins")
data class SkinEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val descricao: String,
    val raritade: String,
    val iconeEmoji: String,
    val precoOuro: Int,
    val desbloqueada: Boolean = false,
    val equipada: Boolean = false
)

// --- DAOS DO ROOM COM REATIVIDADE FLOW E CRUD COMPLETO ---
@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    fun obterProgressoFlow(): Flow<ProgressoEntity?>

    @Upsert
    suspend fun salvarProgresso(progresso: ProgressoEntity)
}

@Dao
interface ItemLojaDao {
    @Query("SELECT * FROM itens_loja")
    fun obterItensLojaFlow(): Flow<List<ItemLojaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirItem(item: ItemLojaEntity)

    @Update
    suspend fun atualizarItem(item: ItemLojaEntity)

    @Delete
    suspend fun deletarItem(item: ItemLojaEntity)
}

@Dao
interface SkinDao {
    @Query("SELECT * FROM skins")
    fun obterSkinsFlow(): Flow<List<SkinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkin(skin: SkinEntity)

    @Update
    suspend fun atualizarSkin(skin: SkinEntity)
}

// --- DATABASE CENTRALIZADO ---
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
