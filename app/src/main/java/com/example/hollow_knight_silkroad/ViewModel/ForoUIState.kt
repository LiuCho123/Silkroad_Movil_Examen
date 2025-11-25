package com.example.hollow_knight_silkroad.ViewModel

import com.example.hollow_knight_silkroad.Model.Hilo

data class ForoUIState(
    val hilosConConteo: List<Pair<Hilo, Int>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)