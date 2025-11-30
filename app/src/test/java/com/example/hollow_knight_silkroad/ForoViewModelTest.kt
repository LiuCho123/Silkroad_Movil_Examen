package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Repository.HiloRepository
import com.example.hollow_knight_silkroad.Repository.RespuestaRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.ForoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val hiloRepository = mockk<HiloRepository>(relaxed = true)
    private val respuestaRepository = mockk<RespuestaRepository>(relaxed = true)
    private val usuarioRepository = mockk<UsuarioRepository>(relaxed = true)

    private lateinit var viewModel: ForoViewModel

    @Test
    fun `cargarHilos exitoso combina hilos y conteo de respuestas`() = runTest {
        val listaHilos = listOf(
            Hilo(idHilo = 1, titulo = "Hilo 1", contenido = "A", autor = "Yo", idUsuario = 1),
            Hilo(idHilo = 2, titulo = "Hilo 2", contenido = "B", autor = "Tú", idUsuario = 2)
        )

        coEvery { hiloRepository.getAllHilos()} returns listaHilos

        coEvery { respuestaRepository.contarRespuestasByHiloId(1) } returns 5
        coEvery { respuestaRepository.contarRespuestasByHiloId(2) } returns 0

        viewModel = ForoViewModel(hiloRepository, respuestaRepository, usuarioRepository)

        testScheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value

        assertFalse(estado.isLoading)
        assertNull(estado.error)
        assertEquals(2, estado.hilosConConteo.size)

        assertEquals(1, estado.hilosConConteo[0].first.idHilo)
        assertEquals(5, estado.hilosConConteo[0].second)

        assertEquals(2, estado.hilosConConteo[1].first.idHilo)
        assertEquals(0, estado.hilosConConteo[1].second)
    }

    @Test
    fun `cargarHilos con error muestra mensaje de error`() = runTest {
        coEvery { hiloRepository.getAllHilos() } throws Exception("Error AWS")

        viewModel = ForoViewModel(hiloRepository, respuestaRepository, usuarioRepository)
        testScheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value

        assertFalse(estado.isLoading)
        assertTrue(estado.hilosConConteo.isEmpty())
        assertNotNull(estado.error)
    }

    @Test
    fun `cerrarSesion llama al repositorio y ejecuta el callback`() = runTest {
        viewModel = ForoViewModel(hiloRepository, respuestaRepository, usuarioRepository)
        var callbackEjecutado = false

        viewModel.cerrarSesion {
            callbackEjecutado = true
        }
        testScheduler.advanceUntilIdle()

        coVerify { usuarioRepository.logout() }
        assertTrue(callbackEjecutado)
    }

    @Test
    fun `refrescarHilos vuelve a llamar a cargarHilos`() = runTest {
        coEvery { hiloRepository.getAllHilos() } returns emptyList()
        viewModel = ForoViewModel(hiloRepository, respuestaRepository, usuarioRepository)
        testScheduler.advanceUntilIdle()

        viewModel.refrescarHilos()
        testScheduler.advanceUntilIdle()

        coVerify(atLeast = 2) { hiloRepository.getAllHilos() }
    }
}