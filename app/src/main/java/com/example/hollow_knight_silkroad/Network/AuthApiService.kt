package com.example.hollow_knight_silkroad.Network

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val login: String,
    val contrasena: String
)

data class LoginResponse(
    val idUsuario: Int,

    @SerializedName("nombreUsuario")
    val usuario: String,

    @SerializedName("correoUsuario")
    val correo: String,
    val rol: String
)

data class RegisterRequest(
    @SerializedName("nombreUsuario")
    val usuario: String,

    @SerializedName("correoUsuario")
    val correo: String,

    @SerializedName("contrasena")
    val contrasena: String
)

data class CambioPasswordRequest(
    val correo: String,

    @SerializedName("nuevaContrasena")
    val nuevaContrasena: String
)

interface AuthApiService{
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/usuarios/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("api/recuperacion/solicitar")
    suspend fun solicitarCodigo(@Body correo: String): Response<ResponseBody>

    @POST("api/recuperacion/cambiar")
    suspend fun cambiarPassword(@Body request: CambioPasswordRequest): Response<ResponseBody>
}