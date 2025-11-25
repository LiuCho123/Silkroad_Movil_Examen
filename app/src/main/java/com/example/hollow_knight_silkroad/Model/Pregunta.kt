package com.example.hollow_knight_silkroad.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pregunta")
data class Pregunta(
    @PrimaryKey(autoGenerate = true)
    val idPregunta: Int = 0,
    val textoPregunta: String
)