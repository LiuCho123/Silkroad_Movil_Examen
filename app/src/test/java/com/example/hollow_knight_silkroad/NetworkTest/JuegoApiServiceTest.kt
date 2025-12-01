package com.example.hollow_knight_silkroad.NetworkTest
import com.example.hollow_knight_silkroad.Network.ActualizarProgresoRequest
import org.junit.Assert.*
import org.junit.Test
class JuegoApiServiceTest {
    @Test
    fun `test ActualizarProgresoRequest properties and copy`() {
        val idUsuario = 5
        val itemId = "boss_grimm"
        val marcado = true

        val request = ActualizarProgresoRequest(idUsuario, itemId, marcado)

        assertEquals(idUsuario, request.idUsuario)
        assertEquals(itemId, request.itemId)
        assertTrue(request.marcado)

        val requestDesmarcado = request.copy(marcado = false)

        assertFalse(requestDesmarcado.marcado)
        assertEquals(request.itemId, requestDesmarcado.itemId)
        assertNotEquals(request, requestDesmarcado)
    }
}