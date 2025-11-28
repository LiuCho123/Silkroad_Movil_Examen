package com.example.hollow_knight_silkroad.Network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class HiloResponse(
    val idHilo: Int,
    val tituloHilo: String,
    val mensajeInicialHilo: String,
    val autorHilo: String,
    val idUsuario: Int,
    val fechaHilo: String?,
    val cantidadRespuesta: Int,

    @SerializedName("imagenBase64")
    val imagenBase64: String?
)

data class CrearHiloRequest(
    @SerializedName("titulo")
    val tituloHilo: String,
    val mensaje: String,
    val idUsuario: Int,
    val nombreAutor: String,
    val imagenBase64: String?
)

data class RespuestaResponse(
    val idRespuesta: Int,
    val mensajeRespuesta: String,
    val autorRespuesta: String,
    val idUsuario: Int,
    val fechaRespuesta: String?,

    @SerializedName("imagenBase64")
    val imagenBase64: String?
)

data class CrearRespuestaRequest(
    val mensaje: String,
    val idUsuario: Int,
    val nombreAutor: String,
    val imagenBase64: String?
)

interface ForoApiService{

    @GET("api/hilos")
    suspend fun obtenerTodos(): Response<List<HiloResponse>>

    @POST("api/hilos")
    suspend fun crearHilo(@Body request: CrearHiloRequest): Response<HiloResponse>

    @GET("api/hilos/{id}")
    suspend fun obtenerHilo(@Path("id") id: Int): Response<HiloResponse>

    @DELETE("api/hilos/{id}")
    suspend fun eliminarHilo(@Path("id") id: Int): Response<String>

    @GET("api/hilos/{id}/respuestas")
    suspend fun obtenerRespuestas(@Path("id") id: Int): Response<List<RespuestaResponse>>

    @POST("api/hilos/{id}/respuestas")
    suspend fun crearRespuesta(@Path("id") id: Int,
                               @Body request: CrearRespuestaRequest): Response<CrearRespuestaRequest>

}