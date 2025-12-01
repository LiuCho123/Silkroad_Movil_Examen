package com.example.hollow_knight_silkroad.ViewModelTest
import com.example.hollow_knight_silkroad.ViewModel.GuiaViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
class GuiaViewModelTest {
    private lateinit var viewModel: GuiaViewModel

    @Before
    fun setup() {
        viewModel = GuiaViewModel()
    }

    @Test
    fun `test initial state loads sections correctly`() {
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.sections.isNotEmpty())
        assertNull(state.expandedSectionId)
    }

    @Test
    fun `toggleSection updates expanded state correctly`() {
        viewModel.toggleSection("0")
        assertEquals("0", viewModel.uiState.value.expandedSectionId)

        viewModel.toggleSection("0")
        assertNull(viewModel.uiState.value.expandedSectionId)

        viewModel.toggleSection("0")
        viewModel.toggleSection("1")
        assertEquals("1", viewModel.uiState.value.expandedSectionId)
    }

    @Test
    fun `expandNextSection moves to the next valid id`() {
        viewModel.toggleSection("0")

        viewModel.expandNextSection("0")

        assertEquals("1", viewModel.uiState.value.expandedSectionId)
    }
}
