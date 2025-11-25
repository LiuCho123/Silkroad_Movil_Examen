package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hollow_knight_silkroad.Model.ChecklistCategory
import com.example.hollow_knight_silkroad.Repository.ChecklistRepository
import com.example.hollow_knight_silkroad.Repository.ChecklistRepositoryDb
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChecklistUiState(
    val categories: List<ChecklistCategory> = emptyList(),
    val checkedItemIds: Set<String> = emptySet(),
    val currentPercentage: Double = 0.0,
    val itemsRemaining: Int = 0,
    val isLoading: Boolean = true
)

class ChecklistViewModel(private val repository: ChecklistRepositoryDb) : ViewModel() {

    val uiState: StateFlow<ChecklistUiState> = repository.checkedIdsFlow
        .map { checkedIdsSet ->
            val percentage = calculatePercentage(checkedIdsSet)
            val remaining = ChecklistRepository.allItems.size - checkedIdsSet.size
            ChecklistUiState(
                categories = ChecklistRepository.allCategories,
                checkedItemIds = checkedIdsSet.toSet(),
                currentPercentage = percentage,
                itemsRemaining = remaining,
                isLoading = false //
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChecklistUiState(
                categories = ChecklistRepository.allCategories,
                itemsRemaining = ChecklistRepository.allItems.size,
                isLoading = true
            )
        )

    private fun calculatePercentage(checkedIds: Collection<String>): Double {
        var percentage = 0.0
        checkedIds.forEach { id ->
            val item = ChecklistRepository.allItems.find { it.id == id }
            if (item != null) {
                percentage += ChecklistRepository.percentageValues[item.category] ?: 0.0
            }
        }
        return percentage
    }

    fun toggleItemChecked(itemId: String) {
        viewModelScope.launch {
            val currentChecked = uiState.value.checkedItemIds
            if (currentChecked.contains(itemId)) {
                repository.uncheckItem(itemId)
            } else {
                repository.checkItem(itemId)
            }
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.clearAllChecked()
        }
    }
}

class ChecklistViewModelFactory(private val repository: ChecklistRepositoryDb) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}