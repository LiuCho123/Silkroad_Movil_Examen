package com.example.hollow_knight_silkroad.Model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistItemDao {
    @Query("SELECT idItem FROM checked_items")
    fun getAllCheckedIdsFlow(): Flow<List<String>> // Return a Flow of IDs

    @Query("SELECT idItem FROM checked_items")
    suspend fun getAllCheckedIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CheckedItem)

    @Query("DELETE FROM checked_items WHERE idItem = :itemId")
    suspend fun deleteById(itemId: String)

    @Query("DELETE FROM checked_items")
    suspend fun deleteAll()
}

