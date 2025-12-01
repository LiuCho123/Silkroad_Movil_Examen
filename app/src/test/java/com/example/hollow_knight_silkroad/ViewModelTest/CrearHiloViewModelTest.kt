package com.example.hollow_knight_silkroad.ViewModelTest
import android.content.Context
import android.net.Uri
import com.example.hollow_knight_silkroad.MainDispatcherRule
import com.example.hollow_knight_silkroad.Repository.HiloRepository
import com.example.hollow_knight_silkroad.ViewModel.CrearHiloViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class CrearHiloViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepository = mockk<HiloRepository>()
    private val mockContext = mockk<Context>()
    private val mockUri = mockk<Uri>()

    private lateinit var viewModel: CrearHiloViewModel

    @Before
    fun setup() {
        viewModel = CrearHiloViewModel(mockRepository)
    }

    @Test
    fun `onInputsChange actualiza el estado correctamente`() {
        val titulo = "Nuevo Hilo"
        val contenido = "Contenido del hilo"
        val uriString = "content://imagen"

        every { mockUri.toString() } returns uriString

        viewModel.onTituloChange(titulo)
        viewModel.onContenidoChange(contenido)
        viewModel.onImagenSeleccionada(mockUri)

        val state = viewModel.uiState.value
        assertEquals(titulo, state.titulo)
        assertEquals(contenido, state.contenido)
        assertEquals(uriString, state.imagenUriSeleccionada)
        assertNull(state.error)
    }

    @Test
    fun `publicarHilo valida campos vacios y muestra error`() = runTest {
        viewModel.publicarHilo(mockContext)

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertEquals("El título y el mensaje con obligatorios", state.error)
    }

    @Test
    fun `publicarHilo llama al repositorio y marca exito`() = runTest {
        // Arrange
        viewModel.onTituloChange("Titulo Valido")
        viewModel.onContenidoChange("Contenido Valido")
        coEvery {
            mockRepository.insertarHilo(any(), any(), any())
        } returns true

        // Act
        viewModel.publicarHilo(mockContext)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.hiloCreadoExitoso)
        assertNull(state.error)
    }

    @Test
    fun `onNavegacionCompletada resetea el estado de exito`() {
        viewModel.onNavegacionCompletada()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.hiloCreadoExitoso)
        assertNull(state.imagenUriSeleccionada)
    }

}