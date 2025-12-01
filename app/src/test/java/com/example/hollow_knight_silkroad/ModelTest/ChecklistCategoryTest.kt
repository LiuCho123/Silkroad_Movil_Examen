package com.example.hollow_knight_silkroad.ModelTest

import com.example.hollow_knight_silkroad.Model.ChecklistCategory
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import org.junit.Test
import org.junit.Assert.assertEquals

class ChecklistCategoryTest {
    @Test
    fun `test ChecklistCategory holds correct data`() {
        val itemsDePrueba = emptyList<ChecklistItem>()
        val categoria = "Bosses"
        val titulo = "Jefes Principales"

        val checklistCategory = ChecklistCategory(
            category = categoria,
            title = titulo,
            items = itemsDePrueba
        )

        assertEquals(categoria, checklistCategory.category)
        assertEquals(titulo, checklistCategory.title )
        assertEquals(itemsDePrueba, checklistCategory.items)
    }
}
