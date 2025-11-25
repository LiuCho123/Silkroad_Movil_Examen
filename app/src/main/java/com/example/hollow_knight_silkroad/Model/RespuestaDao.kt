package com.example.hollow_knight_silkroad.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RespuestaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRespuesta(respuesta: Respuesta)

    @Query("SELECT * FROM respuesta WHERE hiloId = :hiloId ORDER BY fechaCreacion ASC")
    suspend fun getRespuestasByHiloId(hiloId: Int): List<Respuesta>

    @Update
    suspend fun actualizarRespuesta(respuesta: Respuesta)

    @Delete
    suspend fun eliminarRespuesta(respuesta: Respuesta)

    @Query("DELETE FROM respuesta WHERE hiloId = :hiloId")
    suspend fun eliminarRespuestasByHiloId(hiloId: Int)

    @Query("SELECT COUNT(*) FROM respuesta WHERE hiloId = :hiloId")
    suspend fun contarRespuestasByHiloId(hiloId: Int): Int
}