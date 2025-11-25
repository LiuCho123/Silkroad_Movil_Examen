package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.ForoUIState

import com.example.hollow_knight_silkroad.Repository.HiloRepository
import com.example.hollow_knight_silkroad.Repository.RespuestaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForoViewModel(
    private val hiloRepository: HiloRepository,
    private val respuestaRepository: RespuestaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForoUIState())
    val uiState: StateFlow<ForoUIState> = _uiState.asStateFlow()

    init {
        cargarHilos()
    }

    fun cargarHilos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(500)

            try {
                val hilosDesdeRepo = hiloRepository.getAllHilos()

                val conteosAsync = hilosDesdeRepo.map { hilo ->
                    async {
                        respuestaRepository.contarRespuestasByHiloId(hilo.idHilo)
                    }
                }
                val conteos = conteosAsync.awaitAll()

                val hilosConConteo = hilosDesdeRepo.zip(conteos)

                _uiState.update { it.copy(isLoading = false, hilosConConteo = hilosConConteo) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar hilos y conteos.") }
                println("Error en ForoViewModel al cargar Hilos/Conteos: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun refrescarHilos() {
        cargarHilos()
    }
}