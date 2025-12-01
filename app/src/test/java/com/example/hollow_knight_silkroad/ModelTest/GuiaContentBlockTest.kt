package com.example.hollow_knight_silkroad.ModelTest
import com.example.hollow_knight_silkroad.Model.ContentBlock
import com.example.hollow_knight_silkroad.Model.ImageBlock
import com.example.hollow_knight_silkroad.Model.TextBlock
import org.junit.Assert.*
import org.junit.Test

class GuiaContentBlockTest {


    @Test
    fun `test TextBlock creation and default values`() {
        val textBlock = TextBlock(text = "Hola Mundo")

        assertEquals("Hola Mundo", textBlock.text)

        assertFalse(textBlock.isHeading)
    }

    @Test
    fun `test TextBlock with custom heading value`() {
        val textBlock = TextBlock(text = "Título Principal", isHeading = true)

        assertEquals("Título Principal", textBlock.text)
        assertTrue(textBlock.isHeading)
    }


    @Test
    fun `test ImageBlock creation and default values`() {
        val resourceId = 12345

        val imageBlock = ImageBlock(resourceId = resourceId)

        assertEquals(resourceId, imageBlock.resourceId)

        assertNull(imageBlock.contentDescription)
    }

    @Test
    fun `test ImageBlock with content description`() {
        val resourceId = 67890
        val description = "Mapa de Hallownest"

        val imageBlock = ImageBlock(resourceId, description)

        assertEquals(resourceId, imageBlock.resourceId)
        assertEquals(description, imageBlock.contentDescription)
    }

    @Test
    fun `test Polymorphism`() {
        val textBlock: ContentBlock = TextBlock("Text")
        val imageBlock: ContentBlock = ImageBlock(123)

        assertTrue(textBlock is TextBlock)
        assertTrue(imageBlock is ImageBlock)
    }
}