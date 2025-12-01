package com.example.hollow_knight_silkroad.ViewModelTest
import com.example.hollow_knight_silkroad.MainDispatcherRule
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Repository.NetworkChecklistRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.ChecklistViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class ChecklistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockChecklistRepo = mockk<NetworkChecklistRepository>()
    private val mockUsuarioRepo = mockk<UsuarioRepository>()

    private val item1 = ChecklistItem(1, "boss_1", "Boss 1", "Jefes", 1.0, false)
    private val item2 = ChecklistItem(2, "item_2", "Item 2", "Items", 2.0, false)
    private val listaItems = listOf(item1, item2)

    @Test
    fun `init loads data and calculates initial percentage correctly`() = runTest {
        val userId = 10
        val progresoUsuario = listOf("boss_1")

        coEvery { mockChecklistRepo.obtenerChecklist() } returns listaItems
        coEvery { mockUsuarioRepo.obtenerIdUsuarioGuardado() } returns userId
        coEvery { mockChecklistRepo.obtenerProgresoUsuario(userId) } returns progresoUsuario

        val viewModel = ChecklistViewModel(mockChecklistRepo, mockUsuarioRepo)

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.checkedItemIds.contains("boss_1"))
        assertEquals(1.0, state.currentPercentage, 0.0)
    }

    @Test
    fun `toggleItemChecked updates state and calls repository`() = runTest {
        coEvery { mockChecklistRepo.obtenerChecklist() } returns listaItems
        coEvery { mockUsuarioRepo.obtenerIdUsuarioGuardado() } returns 10
        coEvery { mockChecklistRepo.obtenerProgresoUsuario(10) } returns emptyList()
        coEvery { mockChecklistRepo.actualizarProgreso(any(), any(), any()) } returns true

        val viewModel = ChecklistViewModel(mockChecklistRepo, mockUsuarioRepo)

        viewModel.toggleItemChecked("item_2")

        val state = viewModel.uiState.value

        coVerify { mockChecklistRepo.actualizarProgreso(10, "item_2", true) }

        assertTrue(state.checkedItemIds.contains("item_2"))
        assertEquals(2.0, state.currentPercentage, 0.0) // Ahora tenemos el 2.0%
    }

    @Test
    fun `reiniciarProgreso deletes all items and reloads`() = runTest {
        val userId = 10
        val itemsParaBorrar = listOf("boss_1", "item_2")

        coEvery { mockChecklistRepo.obtenerChecklist() } returns listaItems
        coEvery { mockUsuarioRepo.obtenerIdUsuarioGuardado() } returns userId
        coEvery { mockChecklistRepo.obtenerProgresoUsuario(userId) } returnsMany listOf(itemsParaBorrar, itemsParaBorrar, emptyList())
        coEvery { mockChecklistRepo.actualizarProgreso(any(), any(), false) } returns true

        val viewModel = ChecklistViewModel(mockChecklistRepo, mockUsuarioRepo)
        viewModel.reiniciarProgreso()
        coVerify { mockChecklistRepo.actualizarProgreso(userId, "boss_1", false) }
        coVerify { mockChecklistRepo.actualizarProgreso(userId, "item_2", false) }
    }
}