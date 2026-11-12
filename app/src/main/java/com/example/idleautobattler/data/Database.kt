package com.example.idleautobattler.data

import android.content.Context
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Delete
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "progresso")
data class ProgressoEntity(
    @PrimaryKey
    val id: Int = 1,
    val nomeJogador: String = "Guerreiro",
    val ouro: Int = 1250,
    val dano: Int = 15,
    val vidaAtual: Int = 100,
    val vidaMaxima: Int = 100,
    val nivelMonstro: Int = 1,
    val skinEquipada: String = "Herói Padrão"
)

@Entity(tableName = "itens_loja")
data class ItemLojaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val nivelAtual: Int,
    val precoAtual: Int,
    val tipo: String,
    val aumentoDano: Int = 5
)

@Entity(tableName = "skins")
data class SkinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val raritade: String,
    val iconeEmoji: String,
    val precoOuro: Int,
    val desbloqueada: Boolean = false,
    val equipada: Boolean = false
)

@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    fun obterProgressoFlow(): Flow<ProgressoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarProgresso(progresso: ProgressoEntity)

    @Query("UPDATE progresso SET nomeJogador = :nome WHERE id = 1")
    suspend fun atualizarNome(nome: String)

    /**
     * Retorna 1 se o ouro foi descontado; 0 se não havia saldo suficiente
     * ou se o registro de progresso ainda não existe.
     */
    @Query(
        """
        UPDATE progresso
        SET ouro = ouro - :preco
        WHERE id = 1 AND ouro >= :preco
        """
    )
    suspend fun descontarOuroSeSuficiente(preco: Int): Int
}

@Dao
interface ItemLojaDao {
    @Query("SELECT * FROM itens_loja")
    fun obterItensLojaFlow(): Flow<List<ItemLojaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirItem(item: ItemLojaEntity)

    @Delete
    suspend fun deletarItem(item: ItemLojaEntity)

    @Update
    suspend fun atualizarItem(item: ItemLojaEntity)

    @Query("DELETE FROM itens_loja WHERE id = :id")
    suspend fun deletarItemPorId(id: Int)
}

@Dao
interface SkinDao {
    @Query("SELECT * FROM skins ORDER BY nome")
    fun obterSkinsFlow(): Flow<List<SkinEntity>>

    @Query("SELECT * FROM skins WHERE equipada = 1 LIMIT 1")
    fun obterSkinEquipadaFlow(): Flow<SkinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkin(skin: SkinEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkins(skins: List<SkinEntity>)

    @Update
    suspend fun atualizarSkin(skin: SkinEntity)

    @Query("SELECT COUNT(*) FROM skins")
    suspend fun contarSkins(): Int

    @Query("UPDATE skins SET desbloqueada = 1 WHERE id = :skinId")
    suspend fun desbloquearSkin(skinId: Int)

    @Query("UPDATE skins SET equipada = 0")
    suspend fun desequiparTodas()

    @Query(
        """
        UPDATE skins
        SET equipada = 1
        WHERE id = :skinId AND desbloqueada = 1
        """
    )
    suspend fun marcarComoEquipada(skinId: Int)

    @Transaction
    suspend fun equiparSkin(skinId: Int) {
        desequiparTodas()
        marcarComoEquipada(skinId)
    }
}

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
                    // Em desenvolvimento, isso pode apagar o banco se não
                    // houver migração para a nova versão.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}