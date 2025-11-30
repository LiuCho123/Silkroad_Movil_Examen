package com.example.hollow_knight_silkroad.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.Model.ChecklistCategory
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Repository.NetworkChecklistRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChecklistUiState(
    val categories: List<ChecklistCategory> = emptyList(),
    val checkedItemIds: Set<String> = emptySet(),
    val currentPercentage: Double = 0.0,
    val itemsRemaining: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ChecklistViewModel(private val networkChecklistRepository: NetworkChecklistRepository, private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChecklistUiState())
    val uiState: StateFlow<ChecklistUiState> = _uiState.asStateFlow()

    init {
        loadChecklistData()
    }

    private fun loadChecklistData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val checklistItems = networkChecklistRepository.obtenerChecklist()
                val userId = usuarioRepository.obtenerIdUsuarioGuardado()

                val progresoUsuarioObjects = if (userId != -1) {
                    networkChecklistRepository.obtenerProgresoUsuario(userId)
                } else {
                    emptyList()
                }

                val idsCompletados = progresoUsuarioObjects.toSet()
                val updatedItems = checklistItems.map { item ->
                    item.copy(isChecked = idsCompletados.contains(item.itemId))
                }

                val categories = updatedItems.groupBy { it.category }
                    .map { (category, items) ->
                        ChecklistCategory(category = category, title = category, items = items)
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        checkedItemIds = idsCompletados,
                        currentPercentage = calculatePercentage(updatedItems),
                        itemsRemaining = checklistItems.size - idsCompletados.size
                    )
                }
            } catch (e: Exception) {
                Log.e("ChecklistVM", "Error cargando datos", e)
                _uiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }

    private fun calculatePercentage(items: List<ChecklistItem>): Double {
        // Suma directamente el valor de porcentaje de los ítems marcados.
        return items.filter { it.isChecked }.sumOf { it.percentageValue }
    }

    fun toggleItemChecked(itemId: String) {
        viewModelScope.launch {
            val userId = usuarioRepository.obtenerIdUsuarioGuardado()
            if (userId == -1) {
                _uiState.update { it.copy(error = "Usuario no autenticado") }
                return@launch
            }

            val item = findChecklistItem(itemId)
            if (item == null) {
                _uiState.update { it.copy(error = "Ítem no encontrado") }
                return@launch
            }

            val isChecked = _uiState.value.checkedItemIds.contains(itemId)
            val success = networkChecklistRepository.actualizarProgreso(userId, itemId, !isChecked)

            if (success) {
                val updatedCheckedIds = if (isChecked) {
                    _uiState.value.checkedItemIds - itemId
                } else {
                    _uiState.value.checkedItemIds + itemId
                }

                val updatedItems = getUpdatedItemsWithToggle(itemId)

                _uiState.update {
                    it.copy(
                        checkedItemIds = updatedCheckedIds,
                        categories = it.categories.map { category ->
                            category.copy(items = category.items.map { item ->
                                if(item.itemId == itemId) item.copy(isChecked = !item.isChecked) else item
                            })
                        },
                        currentPercentage = calculatePercentage(updatedItems),
                        itemsRemaining = updatedItems.size - updatedCheckedIds.size
                    )
                }
            } else {
                _uiState.update { it.copy(error = "Error al actualizar el progreso") }
            }
        }
    }

    private fun findChecklistItem(itemId: String): ChecklistItem? {
        return _uiState.value.categories.flatMap { it.items }.find { it.itemId == itemId }
    }

    private fun getUpdatedItemsWithToggle(itemId: String): List<ChecklistItem> {
        // Reconstruye la lista completa de ítems con el estado de 'isChecked' actualizado
        return _uiState.value.categories.flatMap { it.items }.map { item ->
            if (item.itemId == itemId) {
                val currentItem = findChecklistItem(itemId)!!
                item.copy(isChecked = !currentItem.isChecked)
            } else {
                item
            }
        }
    }


    fun reiniciarProgreso() {
        viewModelScope.launch {
            val userId = usuarioRepository.obtenerIdUsuarioGuardado()
            if (userId != -1) {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val progresoActualStrings = networkChecklistRepository.obtenerProgresoUsuario(userId)
                    val itemsABorrar = progresoActualStrings
                    val trabajosDeBorrado = itemsABorrar.map { itemId ->
                        async {
                            networkChecklistRepository.actualizarProgreso(
                                idUsuario = userId,
                                itemId = itemId,
                                marcado = false
                            )
                        }
                    }
                    trabajosDeBorrado.awaitAll()
                    loadChecklistData()
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Error al reiniciar: ${e.message}")
                    }
    }}}}
}

class ChecklistViewModelFactory(private val networkChecklistRepository: NetworkChecklistRepository, private val usuarioRepository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistViewModel(networkChecklistRepository, usuarioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
