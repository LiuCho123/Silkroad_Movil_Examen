package com.example.hollow_knight_silkroad.ViewModel

data class CrearHiloUIState(
    val titulo: String = "",
    val contenido: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val hiloCreadoExitoso: Boolean = false,
    val imagenUriSeleccionada: String? = null
)