package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Model.Pregunta
import com.example.hollow_knight_silkroad.Repository.TriviaRepository
import com.example.hollow_knight_silkroad.ViewModel.TriviaViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TriviaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<TriviaRepository>(relaxed = true)
    private lateinit var viewModel: TriviaViewModel

    private val pregunta1 = Pregunta(
        id = 1,
        pregunta = "¿Quién es el protagonista?",
        opciones = listOf("Hornet", "El Caballero", "Zote"),
        respuestaCorrecta = "El Caballero"
    )

    private val pregunta2 = Pregunta(
        id = 2,
        pregunta = "¿Cuál es el arma principal?",
        opciones = listOf("Espada", "Aguijón", "Lanza"),
        respuestaCorrecta = "Aguijón"
    )

    private val listaPreguntas = listOf(pregunta1, pregunta2)

    @Test
    fun `cargarPreguntas exitoso inicializa la lista`() = runTest {
        coEvery { repository.obtenerPreguntas() } returns listaPreguntas

        viewModel = TriviaViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.preguntas.size)
        assertEquals("¿Quién es el protagonista?", state.preguntas[0].pregunta)
    }

    @Test
    fun `cargarPreguntas con error guarda el mensaje`() = runTest {
        coEvery { repository.obtenerPreguntas() } throws Exception("Error API")

        viewModel = TriviaViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Error API", state.error)
    }

    @Test
    fun `handleRespuestaClick correcta aumenta puntaje y avanza`() = runTest {
        setupJuego()

        viewModel.handleRespuestaClick("El Caballero")

        var state = viewModel.uiState.value
        assertEquals(1, state.puntaje)
        assertEquals("El Caballero", state.respuestaSeleccionada)

        advanceTimeBy(1001)

        state = viewModel.uiState.value
        assertEquals(1, state.preguntaActualIndex)
        assertNull(state.respuestaSeleccionada)
    }

    @Test
    fun `handleRespuestaClick incorrecta NO aumenta puntaje`() = runTest {
        setupJuego()

        viewModel.handleRespuestaClick("Zote")

        val state = viewModel.uiState.value
        assertEquals(0, state.puntaje)
        assertEquals("Zote", state.respuestaSeleccionada)

        advanceTimeBy(1001)
        assertEquals(1, viewModel.uiState.value.preguntaActualIndex)
    }

    @Test
    fun `juego termina al responder la ultima pregunta`() = runTest {
        coEvery { repository.obtenerPreguntas() } returns listOf(pregunta1)
        viewModel = TriviaViewModel(repository)
        advanceUntilIdle()

        viewModel.handleRespuestaClick("Cualquiera")
        advanceTimeBy(1001)

        assertTrue(viewModel.uiState.value.juegoTerminado)
    }

    @Test
    fun `reiniciarTrivia vuelve todo a cero`() = runTest {
        setupJuego()
        viewModel.handleRespuestaClick("El Caballero")
        advanceTimeBy(1001)

        viewModel.reiniciarTrivia()

        val state = viewModel.uiState.value
        assertEquals(0, state.preguntaActualIndex)
        assertEquals(0, state.puntaje)
        assertFalse(state.juegoTerminado)
        assertFalse(state.isLoading)
    }

    private fun setupJuego() = runTest {
        coEvery { repository.obtenerPreguntas() } returns listaPreguntas
        viewModel = TriviaViewModel(repository)
        advanceUntilIdle()
    }
}