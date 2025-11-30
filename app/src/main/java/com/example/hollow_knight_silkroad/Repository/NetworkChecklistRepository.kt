package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Model.PuntajeDTO
import com.example.hollow_knight_silkroad.Network.ActualizarProgresoRequest
import com.example.hollow_knight_silkroad.Network.JuegoApiService
import com.example.hollow_knight_silkroad.Network.RetrofitClient
import java.io.IOException

class NetworkChecklistRepository {

    private val juegoApiService: JuegoApiService = RetrofitClient.createService(JuegoApiService::class.java, 8082)

    suspend fun obtenerChecklist(): List<ChecklistItem> {
        val response = juegoApiService.obtenerChecklist()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw IOException("Error al obtener la checklist: ${response.code()}")
        }
    }

    suspend fun obtenerProgresoUsuario(idUsuario: Int): List<String> {
        val response = juegoApiService.obtenerProgresoUsuario(idUsuario)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw IOException("Error al obtener el progreso del usuario: ${response.code()}")
        }
    }

    suspend fun actualizarProgreso(idUsuario: Int, itemId: String, marcado: Boolean): Boolean {
        return try {
            val request = ActualizarProgresoRequest(idUsuario, itemId, marcado)
            val response = juegoApiService.actualizarProgreso(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    suspend fun obtenerRanking(): List<PuntajeDTO> {
        return try {
            val response = juegoApiService.obtenerRanking()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}