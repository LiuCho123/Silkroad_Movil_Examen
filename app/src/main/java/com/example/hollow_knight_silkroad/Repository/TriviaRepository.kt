package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Opcion
import com.example.hollow_knight_silkroad.Model.OpcionDao
import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.Model.PreguntaDao

class TriviaRepository(
    private val preguntaDao: PreguntaDao,
    private val opcionDao: OpcionDao
) {

    suspend fun  getPreguntas(): List<Pregunta>{
        return preguntaDao.getAllPreguntas()
    }

    suspend fun getOpciones(preguntaId: Int): List<Opcion>{
        return opcionDao.getOpcionesByPreguntaId(preguntaId)
    }
}