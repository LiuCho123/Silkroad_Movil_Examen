package com.example.hollow_knight_silkroad.ViewModel

data class RecuperarContrasenaState (
    val email: String = "",
    val codigoIngresado: String = "",
    val codigoGenerado: String? = null,

    val nuevaPassword: String = "",
    val confirmarPassword: String = "",
    val showNuevaPassword: Boolean = false,
    val showConfirmarPassword: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
    val navegacion: String? = null
)