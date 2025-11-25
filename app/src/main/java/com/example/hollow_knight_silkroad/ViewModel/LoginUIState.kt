package com.example.hollow_knight_silkroad.ViewModel

data class LoginUIState(
    val idUsuario: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
    val loginExitoso: Boolean = false
) {
}