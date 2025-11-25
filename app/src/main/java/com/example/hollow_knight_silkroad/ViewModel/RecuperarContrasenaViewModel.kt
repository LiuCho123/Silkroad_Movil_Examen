package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.RecuperarContrasenaState
import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class RecuperarContrasenaViewModel(private val repository: UsuarioRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(RecuperarContrasenaState())
    val uiState: StateFlow<RecuperarContrasenaState> = _uiState.asStateFlow()

    fun onEmailChange(email: String){
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onCodigoChange(codigo: String){
        _uiState.update { it.copy(codigoIngresado = codigo, error = null)}
    }

    fun onNuevaPasswordChange(password: String){
        _uiState.update { it.copy(nuevaPassword = password, error = null)}
    }

    fun onConfirmarPasswordChange(password: String){
        _uiState.update { it.copy(confirmarPassword = password, error = null) }
    }

    fun mostrarNuevaPassword(){
        _uiState.update{it.copy(showNuevaPassword = !it.showNuevaPassword)}
    }

    fun mostrarConfirmarNuevaPassword(){
        _uiState.update { it.copy(showConfirmarPassword = !it.showConfirmarPassword) }
    }

    fun solicitarCodigo(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(1000)

            val emailActual = _uiState.value.email
            if(emailActual.isBlank() || !emailActual.contains("@")){
                _uiState.update{it.copy(error = "Ingrese un correo válido", isLoading = false)}
                return@launch
            }

            val usuarioExiste = repository.findUsuarioByEmail(emailActual) != null

            if (usuarioExiste){
                val codigoGenerado = Random.nextInt(100000, 999999).toString()
                println("Código para $emailActual: $codigoGenerado")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        codigoGenerado = codigoGenerado,
                        navegacion = "verificarCodigo"
                    )
                }
            } else{
                _uiState.update { it.copy(error = "El correo no está registrado", isLoading = false) }
            }
        }
    }

    fun verificarCodigo(){
        val state = _uiState.value
        println("Código Ingresado: '[${state.codigoIngresado}]'") // Ponemos [] para ver espacios
        println("Código Generado: '[${state.codigoGenerado}]'")
        if (state.codigoIngresado.isBlank()){
            _uiState.update { it.copy(error = "Ingrese el código") }
            return
        }

        if (state.codigoIngresado == state.codigoGenerado){
            _uiState.update {
                it.copy(
                    error = null,
                    navegacion = "recuperarPassword"
                )
            }
        } else{
            _uiState.update{it.copy(error = "El código ingresado es incorrecto")}
        }
    }

    fun actualizarPassword(){
        viewModelScope.launch {
            _uiState.update{it.copy(isLoading = true, error = null)}
            delay(1000)

            val state = _uiState.value

            if (state.nuevaPassword.length < 8){
                _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres", isLoading = false) }
                return@launch
            }

            if (state.nuevaPassword != state.confirmarPassword){
                _uiState.update{it.copy(error = "Las contraseñas no coinciden", isLoading = false)}
                return@launch
            }

            val actualizado = repository.updatePasswordByEmail(state.email, state.nuevaPassword)

            if (actualizado){
                println("Contraseña actualizada para ${state.email}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        codigoGenerado = null, codigoIngresado = "",
                        nuevaPassword = "", confirmarPassword = "",
                        navegacion = "login"
                    )
                }
            } else{
                _uiState.update{it.copy(error = "Error al actualizar, usuario no encontrado", isLoading = false)}
            }
        }
    }

    fun onNavegacionCompletada(){
        _uiState.update{it.copy(navegacion = null)}
    }
}