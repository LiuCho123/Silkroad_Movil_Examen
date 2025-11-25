package com.example.hollow_knight_silkroad.Model;

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checked_items")
data class CheckedItem(
        @PrimaryKey val idItem: String
)
