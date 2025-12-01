package com.example.hollow_knight_silkroad.ModelTest
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import org.junit.Assert.*
import org.junit.Test

class ChecklistItemTest {

    @Test
    fun `test ChecklistItem properties and default values`() {
        val item = ChecklistItem(
            id = 1,
            itemId = "boss_false_knight",
            label = "Falso Caballero",
            category = "Bosses",
            percentageValue = 1.0
        )

        assertEquals(1, item.id)
        assertEquals("boss_false_knight", item.itemId)
        assertEquals("Falso Caballero", item.label)
        assertEquals("Bosses", item.category)
        assertEquals(1.0, item.percentageValue, 0.0)

        assertFalse(item.isChecked)

        item.isChecked = true
        assertTrue(item.isChecked)
    }
}