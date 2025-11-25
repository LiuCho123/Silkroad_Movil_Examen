package com.example.hollow_knight_silkroad.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.HiloDetalleUIState
import com.example.hollow_knight_silkroad.Model.Respuesta
import com.example.hollow_knight_silkroad.Repository.HiloRepository
import com.example.hollow_knight_silkroad.Repository.RespuestaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HiloDetalleViewModel(
    private val hiloRepository: HiloRepository,
    private val respuestaRepository: RespuestaRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HiloDetalleUIState())
    val uiState: StateFlow<HiloDetalleUIState> = _uiState.asStateFlow()

    private var hiloIdActual: Int? = null

    fun cargarDetallesHilo(hiloId: Int){
        hiloIdActual = hiloId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try{
                val hilo = hiloRepository.getHiloById(hiloId)
                val respuestas = respuestaRepository.getRespuestasByHiloId(hiloId)

                if (hilo != null){
                    _uiState.update {
                        it.copy(isLoading = false, hilo = hilo, respuestas = respuestas)
                    }
                } else{
                    _uiState.update { it.copy(isLoading = false, error = "Hilo no encontrado") }
                }
            } catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar el hilo") }
                println("Error en HiloDetalleViewModel: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun onNuevaRespuestaChange(contenido: String){
        _uiState.update { it.copy(nuevaRespuestaContenido = contenido, error = null) }
    }

    fun onImagenRespuestaSeleccionada(uri: Uri?){
        _uiState.update { it.copy(imagenUriNuevaRespuesta = uri?.toString()) }
    }
    fun publicarRespuesta(){
        val hiloId = hiloIdActual ?: return
        val state = _uiState.value

        if (state.nuevaRespuestaContenido.isBlank()) {
            _uiState.update { it.copy(error = "La respuesta no puede estar vacía.") }
            return
        }

        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true, error = null) }
            try{
                val nuevaRespuesta = Respuesta(
                    hiloId = hiloId,
                    autor = "UsuarioResponde",
                    contenido = state.nuevaRespuestaContenido,
                    fechaCreacion = System.currentTimeMillis(),
                    imagenUri = state.imagenUriNuevaRespuesta
                )
                respuestaRepository.insertarRespuesta(nuevaRespuesta)

                val respuestasActualizadas = respuestaRepository.getRespuestasByHiloId(hiloId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        respuestas = respuestasActualizadas,
                        nuevaRespuestaContenido = "",
                        imagenUriNuevaRespuesta = null,
                        error = null,
                        respuestaPublicada = true
                    )
                }
            } catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = "Error al publicar la respuesta.") }
                println("Error al publicar respuesta: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun onRespuestaPublicadaHandled(){
        _uiState.update { it.copy(respuestaPublicada = false, imagenUriNuevaRespuesta = null) }
    }

    fun eliminarHiloActual(){
        val hiloId = hiloIdActual ?: return
        val hiloAEliminar = _uiState.value.hilo ?: return

        viewModelScope.launch{
            _uiState.update{ it.copy(isLoading = true)}
            try{
                hiloRepository.eliminarHilo(hiloAEliminar)
                println("Hilo $hiloId eliminado")
                _uiState.update { it.copy(isLoading = false, navegacion = "foro") }
            } catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar el hilo") }
                println("Error al eliminar hilo: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun onNavegacionCompletada(){
        _uiState.update { it.copy(navegacion = null) }
    }
}