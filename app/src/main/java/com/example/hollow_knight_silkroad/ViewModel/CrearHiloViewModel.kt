package com.example.hollow_knight_silkroad.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.CrearHiloUIState
import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Repository.HiloRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CrearHiloViewModel(private val repository: HiloRepository): ViewModel() {
    private val _uiState = MutableStateFlow(CrearHiloUIState())
    val uiState: StateFlow<CrearHiloUIState> = _uiState.asStateFlow()

    fun onTituloChange(titulo: String){
        _uiState.update { it.copy(titulo = titulo, error = null) }
    }

    fun onContenidoChange(contenido: String){
        _uiState.update { it.copy(contenido = contenido, error = null) }
    }

    fun onImagenSeleccionada(uri: Uri?){
        _uiState.update { it.copy(imagenUriSeleccionada = uri?.toString()) }
    }

    fun publicarHilo(context: Context){
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.titulo.isBlank() || currentState.contenido.isBlank()){
                _uiState.update { it.copy(error = "El título y el mensaje con obligatorios") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val nuevoHilo = Hilo(
                idHilo = 0,
                titulo = currentState.titulo,
                contenido = currentState.contenido,
                autor = "LiuCho",
                idUsuario = 0,
                imagenUri = currentState.imagenUriSeleccionada,
                fechaCreacion = System.currentTimeMillis()
            )

            val exito = repository.insertarHilo(
                context = context,
                hilo = nuevoHilo,
                uriImagen = currentState.imagenUriSeleccionada
            )

            if (exito){
                println("Nuevo hilo insertado: ${nuevoHilo.titulo}")
                _uiState.update { it.copy(isLoading = false, hiloCreadoExitoso = true) }
            } else{
                println("Error al insertar hilo")
                _uiState.update { it.copy(isLoading = false, error = "Error al publicar el hilo") }
            }
        }
    }

    fun onNavegacionCompletada(){
        _uiState.update { it.copy(hiloCreadoExitoso = false, imagenUriSeleccionada = null) }
    }
}