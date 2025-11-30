package com.example.hollow_knight_silkroad

import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Repository.UsuarioRepository
import com.example.hollow_knight_silkroad.ViewModel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest{

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val usuarioRepository = mockk<UsuarioRepository>(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    @Test
    fun `login exitoso actualiza estado con loginExitoso = true`() = runTest {
        val usuarioFalso = Usuario(1, "Luis", "luis@gmail.com", "")
        coEvery { usuarioRepository.findUsuarioByIdentifier(any(), any()) } returns usuarioFalso

        viewModel = LoginViewModel(usuarioRepository)
        viewModel.onIdUsuarioChange("Luis")
        viewModel.onPasswordChange("123456")

        viewModel.login()

        testScheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value

        assertFalse(estado.isLoading)
        assertNull(estado.error)
        assertTrue(estado.loginExitoso)
    }

    @Test
    fun `login fallido actualiza estado con mensaje de error`() = runTest {
        coEvery { usuarioRepository.findUsuarioByIdentifier(any(), any()) } returns null

        viewModel = LoginViewModel(usuarioRepository)
        viewModel.onIdUsuarioChange("Luis")
        viewModel.onPasswordChange("Incorrecta")

        viewModel.login()
        testScheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value

        assertFalse(estado.isLoading)
        assertFalse(estado.loginExitoso)
        assertNotNull(estado.error)
        assertEquals("Usuario o contraseña incorrectos", estado.error)
    }

    @Test
    fun `login con campos vacios muestra error inmediato`() = runTest {
        viewModel = LoginViewModel(usuarioRepository)
        viewModel.login()
        testScheduler.advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertEquals("Los campos no pueden estar vacios", estado.error)
    }
}