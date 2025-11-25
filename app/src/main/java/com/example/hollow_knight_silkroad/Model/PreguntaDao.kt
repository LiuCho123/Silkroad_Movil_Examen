package com.example.hollow_knight_silkroad.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreguntaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPreguntas(preguntas: List<Pregunta>)

    @Query("SELECT * FROM pregunta")
    suspend fun getAllPreguntas(): List<Pregunta>

    @Query("SELECT * FROM pregunta WHERE idPregunta = :preguntaId LIMIT 1")
    suspend fun getPreguntaById(preguntaId: Int): Pregunta?
}