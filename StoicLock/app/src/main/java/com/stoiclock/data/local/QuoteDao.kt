package com.stoiclock.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    /** Mis frases primero (más recientes arriba), luego las de fábrica en orden de semilla. */
    @Query("SELECT * FROM quotes ORDER BY isUserCreated DESC, CASE WHEN isUserCreated = 1 THEN -createdAt ELSE id END")
    fun observeAll(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE id = :id")
    fun observeById(id: Long): Flow<QuoteEntity?>

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getById(id: Long): QuoteEntity?

    @Query("SELECT * FROM quotes WHERE includeInLockScreen = 1")
    suspend fun getActive(): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE includeInLockScreen = 1 AND isUserCreated = 1")
    suspend fun getActiveUserCreated(): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE includeInLockScreen = 1 AND isFavorite = 1")
    suspend fun getActiveFavorites(): List<QuoteEntity>

    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): QuoteEntity?

    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(quote: QuoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quotes: List<QuoteEntity>)

    @Update
    suspend fun update(quote: QuoteEntity)

    @Query("DELETE FROM quotes WHERE id = :id AND isUserCreated = 1")
    suspend fun deleteUserCreated(id: Long)

    @Query("UPDATE quotes SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("UPDATE quotes SET includeInLockScreen = :include WHERE id = :id")
    suspend fun setIncludeInLockScreen(id: Long, include: Boolean)
}
