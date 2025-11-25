package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.LoginUIState
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onIdUsuarioChange(idUsuario: String){
        _uiState.update { it.copy(idUsuario = idUsuario, error = null) }
    }

    fun onPasswordChange(password: String){
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun toggleShowPassword(){
        _uiState.update{it.copy(showPassword = !it.showPassword)}
    }

    fun login(){
        viewModelScope.launch {
            _uiState.update{it.copy(isLoading = true, error = null)}
            delay(1000)

            val currentState = _uiState.value
            if (currentState.idUsuario.isBlank() || currentState.password.isBlank()){
                _uiState.update{it.copy(error = "Los campos no pueden estar vacios", isLoading = false)}
                return@launch
            }

            val usuario = repository.findUsuarioByIdentifier(
                currentState.idUsuario,
                currentState.password
            )

            if (usuario != null){
                _uiState.update { it.copy(isLoading = false, error = null, loginExitoso = true) }
                println("Login exitoso para ${usuario.usuario}")
            } else{
                _uiState.update { it.copy(error = "Usuario o contraseña incorrectos", isLoading = false) }
            }
        }
    }

    fun onNavegacionCompletada(){
        _uiState.update{it.copy(loginExitoso = false)}
    }
}