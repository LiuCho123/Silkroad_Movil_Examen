package com.example.hollow_knight_silkroad.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HiloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarHilo(hilo: Hilo)

    @Query("SELECT * FROM hilo ORDER BY idHilo DESC")
    suspend fun getAllHilos(): List<Hilo>

    @Query("SELECT * FROM hilo WHERE idHilo = :idHilo LIMIT 1")
    suspend fun getHiloById(idHilo: Int): Hilo?

    @Update
    suspend fun actualizarHilo(hilo: Hilo)

    @Delete
    suspend fun eliminarHilo(hilo: Hilo)
}