package com.example.hollow_knight_silkroad.ViewModel

import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Model.Respuesta

data class HiloDetalleUIState(
    val hilo: Hilo? = null,
    val respuestas: List<Respuesta> = emptyList(),
    val nuevaRespuestaContenido: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val respuestaPublicada: Boolean = false,
    val navegacion: String? = null,
    val imagenUriNuevaRespuesta: String? = null,
    val esMiHilo: Boolean = false
)