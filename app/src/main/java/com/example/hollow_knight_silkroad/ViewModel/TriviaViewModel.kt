package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.Repository.TriviaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TriviaUIState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val preguntas: List<Pregunta> = emptyList(),
    val preguntaActualIndex: Int = 0,
    val respuestaSeleccionada: String? = null,
    val puntaje: Int = 0,
    val juegoTerminado: Boolean = false
)

class TriviaViewModel(private val repository: TriviaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TriviaUIState())
    val uiState: StateFlow<TriviaUIState> = _uiState.asStateFlow()

    init {
        cargarPreguntas()
    }

    private fun cargarPreguntas() {
        viewModelScope.launch {
            try {
                val preguntas = repository.obtenerPreguntas()
                _uiState.value = TriviaUIState(isLoading = false, preguntas = preguntas)
            } catch (e: Exception) {
                _uiState.value = TriviaUIState(isLoading = false, error = e.message)
            }
        }
    }

    fun handleRespuestaClick(opcion: String) {
        if (_uiState.value.respuestaSeleccionada == null) {
            val preguntaActual = _uiState.value.preguntas[_uiState.value.preguntaActualIndex]
            val esCorrecta = opcion == preguntaActual.respuestaCorrecta

            _uiState.value = _uiState.value.copy(
                respuestaSeleccionada = opcion,
                puntaje = if (esCorrecta) _uiState.value.puntaje + 1 else _uiState.value.puntaje
            )

            viewModelScope.launch {
                kotlinx.coroutines.delay(1000) // Espera 1 segundo
                siguientePregunta()
            }
        }
    }

    private fun siguientePregunta() {
        if (_uiState.value.preguntaActualIndex < _uiState.value.preguntas.size - 1) {
            _uiState.value = _uiState.value.copy(
                preguntaActualIndex = _uiState.value.preguntaActualIndex + 1,
                respuestaSeleccionada = null
            )
        } else {
            _uiState.value = _uiState.value.copy(juegoTerminado = true)
        }
    }

    fun reiniciarTrivia() {
        _uiState.value = TriviaUIState(isLoading = false, preguntas = _uiState.value.preguntas)
    }
}
