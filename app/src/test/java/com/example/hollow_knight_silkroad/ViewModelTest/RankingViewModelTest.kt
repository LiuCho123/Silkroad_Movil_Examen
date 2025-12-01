package com.example.hollow_knight_silkroad.ViewModelTest
import com.example.hollow_knight_silkroad.MainDispatcherRule
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Model.PuntajeDTO
import com.example.hollow_knight_silkroad.Repository.NetworkChecklistRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.RankingViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class RankingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockNetRepo = mockk<NetworkChecklistRepository>()
    private val mockUserRepo = mockk<UsuarioRepository>()

    @Test
    fun `init carga ranking y calcula puntajes diferenciados para usuario actual y otros`() = runTest {
        val myId = 100
        val otherId = 200
        val myName = "Caballero"
        val checklistTotal = listOf(
            ChecklistItem(1, "item_a", "A", "Cat", 1.5, false),
            ChecklistItem(2, "item_b", "B", "Cat", 2.5, false)
        )
        val rankingBackend = listOf(
            PuntajeDTO(myId, 1),
            PuntajeDTO(otherId, 1)
        )
        val myProgress = listOf("item_a")

        coEvery { mockUserRepo.obtenerIdUsuarioGuardado() } returns myId
        coEvery { mockUserRepo.obtenerNombreUsuarioGuardado() } returns myName
        coEvery { mockNetRepo.obtenerRanking() } returns rankingBackend
        coEvery { mockNetRepo.obtenerChecklist() } returns checklistTotal
        coEvery { mockNetRepo.obtenerProgresoUsuario(myId) } returns myProgress
        val viewModel = RankingViewModel(mockNetRepo, mockUserRepo)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.topUsers.size)
        val meUser = state.topUsers.find { it.isCurrentUser }
        assertNotNull(meUser)
        assertEquals("$myName (Tú)", meUser?.name)
        assertEquals(1.5, meUser?.score!!, 0.0)
        val expectedTextMe = String.format("%.1f", 1.5) + "%"
        assertEquals(expectedTextMe, meUser.textoProgreso)
        val otherUser = state.topUsers.find { !it.isCurrentUser }
        assertNotNull(otherUser)
        assertEquals("Usuario $otherId", otherUser?.name)
        assertEquals(50.0, otherUser?.score!!, 0.0)
        val expectedTextOther = String.format("%.1f", 50.0) + "%"
        assertEquals(expectedTextOther, otherUser.textoProgreso)
    }
}