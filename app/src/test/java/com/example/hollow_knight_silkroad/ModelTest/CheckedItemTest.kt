package com.example.hollow_knight_silkroad.ModelTest
import com.example.hollow_knight_silkroad.Model.CheckedItem
import org.junit.Assert.*
import org.junit.Test

class CheckedItemTest {

    @Test
    fun `create CheckedItem and verify properties`() {
        val expectedId = "item_123"

        val checkedItem = CheckedItem(expectedId)


        assertEquals(expectedId, checkedItem.idItem)
    }

    @Test
    fun `test data class equality`() {
        val item1 = CheckedItem("boss_hornet")
        val item2 = CheckedItem("boss_hornet")
        val item3 = CheckedItem("boss_hk")

        assertEquals(item1, item2)
        assertNotEquals(item1, item3)

        assertEquals(item1.hashCode(), item2.hashCode())
    }
}