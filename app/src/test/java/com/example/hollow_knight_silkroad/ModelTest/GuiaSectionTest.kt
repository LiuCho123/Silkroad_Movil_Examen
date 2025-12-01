package com.example.hollow_knight_silkroad.ModelTest
import com.example.hollow_knight_silkroad.Model.ContentBlock
import com.example.hollow_knight_silkroad.Model.GuiaSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
class GuiaSectionTest {
    @Test
    fun `test GuiaSection constructor and copy`() {
        val id = "section_intro"
        val title = "Introducción"
        val contentBlocks = emptyList<ContentBlock>()
        val section = GuiaSection(id, title, contentBlocks)

        assertEquals(id, section.id)
        assertEquals(title, section.title)
        assertEquals(contentBlocks, section.contentBlocks)

        val modifiedSection = section.copy(title = "Conclusión")

        assertEquals("Conclusión", modifiedSection.title) // El título cambió
        assertEquals(section.id, modifiedSection.id)      // El ID se mantuvo igual
        assertNotEquals(section, modifiedSection)         // Son objetos diferentes
    }
}