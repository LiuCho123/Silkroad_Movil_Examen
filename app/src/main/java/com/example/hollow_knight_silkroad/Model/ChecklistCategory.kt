package com.example.hollow_knight_silkroad.Model

data class ChecklistCategory(
    val category: String,
    val title : String,
    val items : List<ChecklistItem>
)