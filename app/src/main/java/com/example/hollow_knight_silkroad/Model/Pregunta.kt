package com.example.hollow_knight_silkroad.Model

import com.google.gson.annotations.SerializedName

data class Pregunta(
    val id: Int,

    @SerializedName("pregunta")
    val pregunta: String,

    @SerializedName("opciones")
    val opciones: List<String>,

    @SerializedName("respuestaCorrecta")
    val respuestaCorrecta: String
)