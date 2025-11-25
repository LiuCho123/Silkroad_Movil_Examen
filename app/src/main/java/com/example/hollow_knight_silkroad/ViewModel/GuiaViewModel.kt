package com.example.hollow_knight_silkroad.ViewModel
import androidx.lifecycle.ViewModel
import com.example.hollow_knight_silkroad.Repository.GuiaRepository
import com.example.hollow_knight_silkroad.Model.GuiaSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GuiaUiState(
    val sections: List<GuiaSection> = emptyList(),
    val isLoading: Boolean = true,
    val expandedSectionId: String? = null
)

class GuiaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GuiaUiState())
    val uiState: StateFlow<GuiaUiState> = _uiState.asStateFlow()

    init {
        loadSections()
    }

    private fun loadSections() {
        _uiState.value = GuiaUiState(
            sections = GuiaRepository.sections,
            isLoading = false
        )
    }
    fun toggleSection(sectionId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                expandedSectionId = if (currentState.expandedSectionId == sectionId) null else sectionId
            )
        }
    }    fun expandNextSection(currentId: String) {
        val sections = _uiState.value.sections
        val currentIndex = sections.indexOfFirst { it.id == currentId }
        val nextSectionId = sections.getOrNull(currentIndex + 1)?.id
        if (nextSectionId != null) {
            _uiState.update { it.copy(expandedSectionId = nextSectionId) }
        } else {
            _uiState.update { it.copy(expandedSectionId = null) }
        }
    }
}