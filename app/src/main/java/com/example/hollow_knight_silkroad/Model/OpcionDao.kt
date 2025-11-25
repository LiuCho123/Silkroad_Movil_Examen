package com.example.hollow_knight_silkroad.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OpcionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOpciones(opciones: List<Opcion>)

    @Query("SELECT * FROM opcion WHERE preguntaId = :preguntaId")
    suspend fun getOpcionesByPreguntaId(preguntaId: Int): List<Opcion>
}