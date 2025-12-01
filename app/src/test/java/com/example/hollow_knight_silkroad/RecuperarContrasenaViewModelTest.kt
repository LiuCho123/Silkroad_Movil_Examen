package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.RecuperarContrasenaViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecuperarContrasenaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<UsuarioRepository>(relaxed = true)
    private lateinit var viewModel: RecuperarContrasenaViewModel

    @Test
    fun `solicitarCodigo exitoso guarda codigo y navega`() = runTest {
        coEvery { repository.solicitarCodigoRecuperacion("test@mail.com") } returns "123456"

        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onEmailChange("test@mail.com")

        viewModel.solicitarCodigo()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("123456", state.codigoGenerado)
        assertEquals("verificarCodigo", state.navegacion)
        assertNull(state.error)
    }

    @Test
    fun `solicitarCodigo fallido muestra error`() = runTest {
        coEvery { repository.solicitarCodigoRecuperacion(any()) } returns null

        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onEmailChange("test@mail.com")

        viewModel.solicitarCodigo()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("El correo no está registrado", state.error)
        assertNull(state.navegacion)
    }

    @Test
    fun `verificarCodigo correcto navega al siguiente paso`() = runTest {
        coEvery { repository.solicitarCodigoRecuperacion(any()) } returns "111111"
        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onEmailChange("a@a.com")
        viewModel.solicitarCodigo()
        advanceUntilIdle()

        viewModel.onCodigoChange("111111")
        viewModel.verificarCodigo()

        val state = viewModel.uiState.value
        assertNull(state.error)
        assertEquals("recuperarPassword", state.navegacion)
    }

    @Test
    fun `verificarCodigo incorrecto muestra error`() = runTest {
        coEvery { repository.solicitarCodigoRecuperacion(any()) } returns "111111"
        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onEmailChange("a@a.com")
        viewModel.solicitarCodigo()
        advanceUntilIdle()

        viewModel.onCodigoChange("999999")
        viewModel.verificarCodigo()

        val state = viewModel.uiState.value
        assertEquals("El código ingresado es incorrecto", state.error)
        assertNotEquals("recuperarPassword", state.navegacion)
    }

    @Test
    fun `actualizarPassword exitoso navega al login`() = runTest {
        coEvery { repository.updatePasswordByEmail(any(), any()) } returns true

        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onEmailChange("test@mail.com")
        viewModel.onNuevaPasswordChange("12345678")
        viewModel.onConfirmarPasswordChange("12345678")

        viewModel.actualizarPassword()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("login", state.navegacion)
        assertEquals("", state.nuevaPassword)
    }

    @Test
    fun `actualizarPassword con passwords distintas falla`() = runTest {
        viewModel = RecuperarContrasenaViewModel(repository)
        viewModel.onNuevaPasswordChange("12345678")
        viewModel.onConfirmarPasswordChange("87654321")

        viewModel.actualizarPassword()
        advanceUntilIdle()

        assertEquals("Las contraseñas no coinciden", viewModel.uiState.value.error)
        coVerify(exactly = 0) { repository.updatePasswordByEmail(any(), any()) }
    }
}