package com.example.hollow_knight_silkroad.ViewModel

import com.example.hollow_knight_silkroad.Model.Opcion
import com.example.hollow_knight_silkroad.Model.Pregunta

data class PreguntaConOpciones(
    val pregunta: Pregunta,
    val opciones: List<Opcion>
)

data class TriviaUIState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val preguntas: List<PreguntaConOpciones> = emptyList(),
    val preguntaActualIndex: Int = 0,
    val puntaje: Int = 0,
    val juegoTerminado: Boolean = false,
    val respuestaSeleccionada: Opcion? = null
)