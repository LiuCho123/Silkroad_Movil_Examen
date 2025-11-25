package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.ViewModel.RegisterUIState
import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UsuarioRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun onEmailChange(email: String){
        _uiState.update{it.copy(email = email, error = null)}
    }

    fun onUsernameChange(username: String){
        _uiState.update{it.copy(username = username, error = null)}
    }

    fun onPasswordChange(password: String){
        _uiState.update{it.copy(password = password, error = null)}
    }

    fun onConfirmPasswordChange(confirmPassword: String){
        _uiState.update{it.copy(confirmPassword = confirmPassword, error = null)}
    }

    fun toggleShowPassword(){
        _uiState.update{it.copy(showPassword = !it.showPassword)}
    }

    fun toggleShowConfirmPassword(){
        _uiState.update{it.copy(showConfirmPassword = !it.showConfirmPassword)}
    }

    fun register(){
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.email.isBlank() || currentState.username.isBlank()
                || currentState.password.isBlank()){
                _uiState.update{it.copy(error = "Todos los campos son obligatorios")}
                return@launch
            }
            if (!currentState.email.contains("@")){
                _uiState.update { it.copy(error = "El formato del correo no es válido") }
                return@launch
            }
            if (currentState.username.length < 6){
                _uiState.update { it.copy(error = "El nombre de usuario debe tener al menos 6 caracteres") }
                return@launch
            }
            if (currentState.password.length < 8){
                _uiState.update{it.copy(error = "La contraseña debe tener al menos 8 caracteres")}
                return@launch
            }
            if(currentState.password != currentState.confirmPassword){
                _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(1000)

            val nuevoUsuario = Usuario(
                idUsuario = 0,
                usuario = currentState.username,
                correo = currentState.email,
                contrasena = currentState.password
            )

            val registroExitoso = repository.addUsuario(nuevoUsuario)

            if (registroExitoso){
                println("Registro exitoso para ${currentState.username}")
                _uiState.update{it.copy(isLoading = false, error = null, registroExitoso = true)}
            } else{
                _uiState.update{it.copy(error = "El nombre de usuario o correo ya está en uso", isLoading = false)}
            }
        }
    }

    fun onNavegacionCompletada(){
        _uiState.update{it.copy(registroExitoso = false)}
    }
}
