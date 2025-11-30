package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.Network.JuegoApiService
import com.example.hollow_knight_silkroad.Network.RetrofitClient

class TriviaRepository {

    private val apiService = RetrofitClient.createService(JuegoApiService::class.java, 8082)

    suspend fun obtenerPreguntas(): List<Pregunta> {
        return try {
            val response = apiService.obtenerTrivia()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}