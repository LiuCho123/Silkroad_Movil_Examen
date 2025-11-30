package com.example.hollow_knight_silkroad.Model // Ajusta a tu paquete real

import com.google.gson.annotations.SerializedName

data class PuntajeDTO(
    @SerializedName("idUsuario")
    val idUsuario: Int,

    @SerializedName("cantidadItems")
    val cantidadItems: Long
)