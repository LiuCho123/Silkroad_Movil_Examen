package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Model.Respuesta
import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Repository.HiloRepository
import com.example.hollow_knight_silkroad.Repository.RespuestaRepository
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.HiloDetalleViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HiloDetalleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val hiloRepository = mockk<HiloRepository>(relaxed = true)
    private val respuestaRepository = mockk<RespuestaRepository>(relaxed = true)

    private lateinit var viewModel: HiloDetalleViewModel

    private val miUsuario = Usuario(idUsuario = 100, usuario = "Yo", correo = "yo@mail.com", contrasena = "")
    private val hiloMio = Hilo(idHilo = 1, titulo = "Mio", contenido = "A", autor = "Yo", idUsuario = 100) // ID 100 == ID 100
    private val hiloAjeno = Hilo(idHilo = 2, titulo = "Ajeno", contenido = "B", autor = "Otro", idUsuario = 999) // ID 999 != ID 100

    @After
    fun tearDown() {
        UsuarioRepository.usuarioActual = null
    }

    @Test
    fun `cargarDetallesHilo detecta que SOY el dueno`() = runTest {
        UsuarioRepository.usuarioActual = miUsuario
        coEvery { hiloRepository.getHiloById(1) } returns hiloMio
        coEvery { respuestaRepository.getRespuestasByHiloId(1) } returns emptyList()

        viewModel = HiloDetalleViewModel(hiloRepository, respuestaRepository)

        viewModel.cargarDetallesHilo(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(hiloMio, state.hilo)
        assertTrue("Debería reconocer que es mi hilo", state.esMiHilo)
    }

    @Test
    fun `cargarDetallesHilo detecta que NO soy el dueno`() = runTest {
        UsuarioRepository.usuarioActual = miUsuario
        coEvery { hiloRepository.getHiloById(2) } returns hiloAjeno

        viewModel = HiloDetalleViewModel(hiloRepository, respuestaRepository)

        viewModel.cargarDetallesHilo(2)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(hiloAjeno, state.hilo)
        assertFalse("NO debería ser mi hilo", state.esMiHilo)
    }

    @Test
    fun `publicarRespuesta inserta y recarga la lista`() = runTest {
        coEvery { hiloRepository.getHiloById(1) } returns hiloMio
        viewModel = HiloDetalleViewModel(hiloRepository, respuestaRepository)
        viewModel.cargarDetallesHilo(1)
        advanceUntilIdle()

        viewModel.onNuevaRespuestaChange("Mi respuesta")

        val contextMock = mockk<android.content.Context>(relaxed = true)
        viewModel.publicarRespuesta(contextMock)
        advanceUntilIdle()

        coVerify { respuestaRepository.insertarRespuesta(any(), any(), any()) }
        coVerify(atLeast = 1) { respuestaRepository.getRespuestasByHiloId(1) }

        assertTrue(viewModel.uiState.value.respuestaPublicada)
        assertEquals("", viewModel.uiState.value.nuevaRespuestaContenido)
    }

    @Test
    fun `eliminarHiloActual llama a eliminar y navega`() = runTest {
        // GIVEN
        coEvery { hiloRepository.getHiloById(1) } returns hiloMio
        viewModel = HiloDetalleViewModel(hiloRepository, respuestaRepository)
        viewModel.cargarDetallesHilo(1)
        advanceUntilIdle()

        viewModel.eliminarHiloActual()
        advanceUntilIdle()

        coVerify { hiloRepository.eliminarHilo(any()) }
        assertEquals("foro", viewModel.uiState.value.navegacion)
    }
}