package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.Repository.NetworkChecklistRepository
import com.example.hollow_knight_silkroad.Repository.SpeedrunRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RankingUser(
    val name: String,
    val score: Double,
    val textoProgreso: String,
    val isCurrentUser: Boolean = false
)

data class RankingUiState(
    val topUsers: List<RankingUser> = emptyList(),
    val isLoading: Boolean = true,
    val speedrunRecord: String = "Cargando..."
)

class RankingViewModel(
    private val networkChecklistRepository: NetworkChecklistRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    private val speedrunRepository = SpeedrunRepository()

    init {
        cargarRecordMundial()
        cargarRanking()
    }

    fun cargarRanking() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val myId = usuarioRepository.obtenerIdUsuarioGuardado()
                val myName = usuarioRepository.obtenerNombreUsuarioGuardado() ?: "Yo"

                val rankingDeferred = async { networkChecklistRepository.obtenerRanking() }
                val checklistDeferred = async { networkChecklistRepository.obtenerChecklist() }

                val myProgressDeferred = async {
                    if (myId != -1) networkChecklistRepository.obtenerProgresoUsuario(myId)
                    else emptyList()
                }

                val rankingDtos = rankingDeferred.await()
                val allChecklistItems = checklistDeferred.await()
                val myCheckedIds = myProgressDeferred.await().toSet()

                val totalItemsCount = allChecklistItems.size.toDouble()

                val rankingUsers = rankingDtos.map { dto ->
                    val esMiUsuario = dto.idUsuario == myId

                    val porcentajeFinal: Double

                    if (esMiUsuario) {
                        porcentajeFinal = allChecklistItems
                            .filter { it.itemId in myCheckedIds }
                            .sumOf { it.percentageValue }
                    } else {
                        porcentajeFinal = if (totalItemsCount > 0) {
                            (dto.cantidadItems.toDouble() / totalItemsCount) * 100
                        } else {
                            0.0
                        }
                    }

                    RankingUser(
                        name = if (esMiUsuario) "$myName (Tú)" else "Usuario ${dto.idUsuario}",
                        score = porcentajeFinal,
                        textoProgreso = "${String.format("%.1f", porcentajeFinal)}%",
                        isCurrentUser = esMiUsuario
                    )
                }

                _uiState.update {
                    it.copy(
                        topUsers = rankingUsers.sortedByDescending { u -> u.score },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun cargarRecordMundial() {
        viewModelScope.launch {
            try {
                val record = speedrunRepository.obtenerRecordMundial()
                _uiState.update { it.copy(speedrunRecord = record) }
            } catch (e: Exception) {
                _uiState.update { it.copy(speedrunRecord = "No disponible") }
            }
        }
    }

    class RankingViewModelFactory(
        private val networkChecklistRepository: NetworkChecklistRepository,
        private val usuarioRepository: UsuarioRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RankingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RankingViewModel(networkChecklistRepository, usuarioRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}