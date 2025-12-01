package com.example.hollow_knight_silkroad.ModelTest
import com.example.hollow_knight_silkroad.Model.PuntajeDTO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PuntajeDTOTest {
    @Test
    fun `test PuntajeDTO construction and modification`() {
        val idUsuario = 10
        val cantidadItems = 45L
        val puntajeDTOTest = PuntajeDTO(idUsuario, cantidadItems)

        assertEquals(idUsuario, puntajeDTOTest.idUsuario)
        assertEquals(cantidadItems, puntajeDTOTest.cantidadItems)

        val updatedPuntaje = puntajeDTOTest.copy(cantidadItems = 50L)

        assertEquals(50L, updatedPuntaje.cantidadItems) // El valor cambió
        assertEquals(puntajeDTOTest.idUsuario, updatedPuntaje.idUsuario) // El ID se mantiene
        assertNotEquals(puntajeDTOTest, updatedPuntaje) // Son objetos distintos
    }
}