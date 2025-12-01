package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.RegistroViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val usuarioRepository = mockk<UsuarioRepository>(relaxed = true)
    private lateinit var viewModel: RegistroViewModel


    @Test
    fun `register con campos vacios muestra error`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        // No llenamos nada
        viewModel.register()
        advanceUntilIdle()

        assertEquals("Todos los campos son obligatorios", viewModel.uiState.value.error)
    }

    @Test
    fun `register con email invalido muestra error`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos() // Llenamos todo bien primero
        viewModel.onEmailChange("correosin-arroba.com")

        viewModel.register()
        advanceUntilIdle()

        assertEquals("El formato del correo no es válido", viewModel.uiState.value.error)
    }

    @Test
    fun `register con usuario corto muestra error`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos()
        viewModel.onUsernameChange("corto")

        viewModel.register()
        advanceUntilIdle()

        assertEquals("El nombre de usuario debe tener al menos 6 caracteres", viewModel.uiState.value.error)
    }

    @Test
    fun `register con password corta muestra error`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos()
        viewModel.onPasswordChange("123")

        viewModel.register()
        advanceUntilIdle()

        assertEquals("La contraseña debe tener al menos 8 caracteres", viewModel.uiState.value.error)
    }

    @Test
    fun `register con passwords distintas muestra error`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos()
        viewModel.onConfirmPasswordChange("otra-cosa-nada-que-ver")

        viewModel.register()
        advanceUntilIdle()

        assertEquals("Las contraseñas no coinciden", viewModel.uiState.value.error)
    }


    @Test
    fun `register exitoso llama al repo y actualiza estado`() = runTest {
        coEvery { usuarioRepository.addUsuario(any()) } returns true

        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos()

        viewModel.register()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.registroExitoso)
        assertNull(state.error)
    }

    @Test
    fun `register fallido (usuario existe) muestra error del repo`() = runTest {
        coEvery { usuarioRepository.addUsuario(any()) } returns false

        viewModel = RegistroViewModel(usuarioRepository)
        llenarCamposValidos()

        viewModel.register()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.registroExitoso)
        assertEquals("El nombre de usuario o correo ya está en uso", state.error)
    }

    @Test
    fun `toggleShowPassword cambia el estado visual`() = runTest {
        viewModel = RegistroViewModel(usuarioRepository)
        assertFalse(viewModel.uiState.value.showPassword)

        viewModel.toggleShowPassword()
        assertTrue(viewModel.uiState.value.showPassword)
    }

    private fun llenarCamposValidos() {
        viewModel.onEmailChange("test@mail.com")
        viewModel.onUsernameChange("usuario123")
        viewModel.onPasswordChange("12345678")
        viewModel.onConfirmPasswordChange("12345678")
    }
}