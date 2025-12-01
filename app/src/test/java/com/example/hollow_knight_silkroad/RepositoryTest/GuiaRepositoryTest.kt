package com.example.hollow_knight_silkroad.RepositoryTest
import com.example.hollow_knight_silkroad.Model.TextBlock
import com.example.hollow_knight_silkroad.Repository.GuiaRepository
import org.junit.Assert.*
import org.junit.Test
class GuiaRepositoryTest {
    @Test
    fun `test sections list is initialized with correct data`() {
        val sections = GuiaRepository.sections

        assertTrue("La lista de secciones no debería estar vacía", sections.isNotEmpty())

        val firstSection = sections.find { it.id == "0" }

        assertNotNull("Debería existir la sección con ID 0", firstSection)
        assertEquals("El viaje a Bocasucia", firstSection?.title)

        assertTrue("La sección debe tener bloques de contenido", firstSection!!.contentBlocks.isNotEmpty())

        assertTrue(firstSection.contentBlocks[0] is TextBlock)
    }
}