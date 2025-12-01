package com.example.hollow_knight_silkroad.RepositoryTest
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Network.JuegoApiService
import com.example.hollow_knight_silkroad.Network.RetrofitClient
import com.example.hollow_knight_silkroad.Repository.NetworkChecklistRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
class NetworkChecklistRepositoryTest {
    private lateinit var mockApiService: JuegoApiService

    @Before
    fun setup() {
        mockkObject(RetrofitClient)

        mockApiService = mockk()

        every {
            RetrofitClient.createService(JuegoApiService::class.java, 8082)
        } returns mockApiService
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `obtenerChecklist devuelve lista correctamente`() = runTest {
        val datosFalsos = listOf(
            ChecklistItem(1, "item_test", "Prueba", "Test", 1.0, false)
        )

        coEvery { mockApiService.obtenerChecklist() } returns Response.success(datosFalsos)

        val repository = NetworkChecklistRepository()

        val resultado = repository.obtenerChecklist()

        assertEquals(1, resultado.size)           // ¿Llegó 1 ítem?
        assertEquals("item_test", resultado[0].itemId) // ¿Es el ítem correcto?
    }
}
