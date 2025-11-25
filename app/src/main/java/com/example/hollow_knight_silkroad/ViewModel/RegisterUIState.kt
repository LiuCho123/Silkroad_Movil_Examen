package com.example.hollow_knight_silkroad.ViewModel

data class RegisterUIState (
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false,
    val isLoading: Boolean = false
)