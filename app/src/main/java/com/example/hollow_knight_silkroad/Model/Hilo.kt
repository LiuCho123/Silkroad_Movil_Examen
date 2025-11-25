package com.example.hollow_knight_silkroad.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hilo")
data class Hilo (
    @PrimaryKey(autoGenerate = true)
    val idHilo: Int = 0,
    val titulo: String = "",
    val contenido: String,
    val autor: String,
    val imagenUri: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)