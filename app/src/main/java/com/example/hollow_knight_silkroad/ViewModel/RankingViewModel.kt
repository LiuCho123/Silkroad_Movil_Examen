package com.example.hollow_knight_silkroad.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RankingUser(
    val name: String,
    val score: Double,
    val isCurrentUser: Boolean = false
)

data class RankingUiState(
    val topUsers: List<RankingUser> = emptyList(),
    val isLoading: Boolean = true
)


class RankingViewModel(
    private val checklistViewModel: ChecklistViewModel ) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    private val currentUsername = "Liucho"

    init {
        viewModelScope.launch {
            checklistViewModel.uiState.collect { checklistState ->
                if (!checklistState.isLoading) {
                    prepareRanking(checklistState.currentPercentage)
                }
            }
        }
    }

    private fun prepareRanking(currentUserPercentage: Double) {
        // Usuarios simulados
        val simulatedUsers = listOf(
            RankingUser(name = "Hornet", score = 112.00),
            RankingUser(name = "Zote el Todopoderoso", score = 111.75),
            RankingUser(name = "Quirrel", score = 98.50),
            RankingUser(name = "Cloth", score = 85.25),
            RankingUser(name = "Cornifer", score = 50.75),
            RankingUser(name = "Iselda", score = 25.00),
        )

        val currentUser = RankingUser(
            name = "$currentUsername (Tú)",
            score = currentUserPercentage,
            isCurrentUser = true
        )

        val allUsers = (simulatedUsers + currentUser)
            .sortedByDescending { it.score }
            .take(5)

        _uiState.update {
            it.copy(topUsers = allUsers, isLoading = false)
        }
    }
}