package com.example.hollow_knight_silkroad.Network

import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.Model.PuntajeDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class ActualizarProgresoRequest(
    val idUsuario: Int,
    val itemId: String,
    val marcado: Boolean
)

interface JuegoApiService {

    @GET("api/checklist")
    suspend fun obtenerChecklist(): Response<List<ChecklistItem>>

    @GET("api/checklist/progreso/{idUsuario}")
    suspend fun obtenerProgresoUsuario(@Path("idUsuario") idUsuario: Int): Response<List<String>>

    @POST("api/checklist/progreso")
    suspend fun actualizarProgreso(@Body request: ActualizarProgresoRequest): Response<Void>
    @GET("api/trivia")
    suspend fun obtenerTrivia(): Response<List<Pregunta>>
    @GET("api/checklist/ranking")
    suspend fun obtenerRanking(): Response<List<PuntajeDTO>>
}

