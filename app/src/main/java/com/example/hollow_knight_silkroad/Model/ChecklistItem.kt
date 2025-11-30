package com.example.hollow_knight_silkroad.Model
import com.google.gson.annotations.SerializedName

data class ChecklistItem(
    val id: Int,

    @SerializedName("itemId")
    val itemId: String,
    val label: String,
    val category: String,
    @SerializedName("percentageValue")
    val percentageValue: Double,
    var isChecked: Boolean = false
)