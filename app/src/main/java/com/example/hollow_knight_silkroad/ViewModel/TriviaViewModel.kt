package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.Model.Opcion
import com.example.hollow_knight_silkroad.Repository.TriviaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TriviaViewModel(private val repository: TriviaRepository): ViewModel() {
    private val _uiState = MutableStateFlow(TriviaUIState())
    val uiState: StateFlow<TriviaUIState> = _uiState.asStateFlow()

    init {
        cargarPreguntas()
    }

    private fun cargarPreguntas(){
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true, error = null) }
            try{
                var preguntas = repository.getPreguntas()
                var intentos = 0

                while(preguntas.isEmpty() && intentos < 5){
                    delay(500) // Espera
                    preguntas = repository.getPreguntas()
                    intentos++
                }

                val preguntasConOpciones = preguntas.map{ pregunta ->
                    val opciones = repository.getOpciones(pregunta.idPregunta)
                    PreguntaConOpciones(pregunta = pregunta, opciones = opciones.shuffled())
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    preguntas = preguntasConOpciones.shuffled()
                ) }

            } catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar la trivia") }
                println("Error en TriviaViewModel: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun handleRespuestaClick(opcionSeleccionada: Opcion){
        if (_uiState.value.respuestaSeleccionada != null) return

        var puntajeActual = _uiState.value.puntaje
        if(opcionSeleccionada.esCorrecta){
            puntajeActual++
        }

        _uiState.update{
            it.copy(
                respuestaSeleccionada = opcionSeleccionada,
                puntaje = puntajeActual
            )
        }

        viewModelScope.launch{
            delay(1500)
            siguientePregunta()
        }
    }

    private fun siguientePregunta(){
        val state = _uiState.value
        val siguienteIndex = state.preguntaActualIndex + 1

        if (siguienteIndex < state.preguntas.size){
            _uiState.update { it.copy(
                preguntaActualIndex = siguienteIndex,
                respuestaSeleccionada = null
            ) }
        } else{
            _uiState.update { it.copy(juegoTerminado = true) }
        }
    }

    fun reiniciarTrivia(){
        cargarPreguntas()
        _uiState.update {
            it.copy(
                preguntaActualIndex = 0,
                puntaje = 0,
                juegoTerminado = false,
                respuestaSeleccionada = null,
                isLoading = true
            )
        }
    }
}