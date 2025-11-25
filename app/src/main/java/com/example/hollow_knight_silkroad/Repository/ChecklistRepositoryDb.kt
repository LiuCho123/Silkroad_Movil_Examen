package com.example.hollow_knight_silkroad.Repository
import com.example.hollow_knight_silkroad.Model.CheckedItem
import com.example.hollow_knight_silkroad.Model.ChecklistCategory
import com.example.hollow_knight_silkroad.Model.ChecklistItem
import com.example.hollow_knight_silkroad.Model.ChecklistItemDao
import kotlinx.coroutines.flow.Flow

class ChecklistRepositoryDb(private val checkedItemDao: ChecklistItemDao) {
    val checkedIdsFlow: Flow<List<String>> = checkedItemDao.getAllCheckedIdsFlow()
    suspend fun checkItem(itemId: String) {
        checkedItemDao.insert(CheckedItem(idItem = itemId))
    }
    suspend fun uncheckItem(itemId: String) {
        checkedItemDao.deleteById(itemId)
    }
    suspend fun clearAllChecked() {
        checkedItemDao.deleteAll()
    }
    val percentageValues = mapOf(
        "boss" to 1.0, "equip" to 2.0,
    )
    val allCategories: List<ChecklistCategory> = listOf(
        ChecklistCategory("boss", "Jefes", listOf(  )),
    )
    val allItems: List<ChecklistItem> = allCategories.flatMap { it.items }
}