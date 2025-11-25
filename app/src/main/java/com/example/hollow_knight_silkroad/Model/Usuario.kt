package com.example.hollow_knight_silkroad.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Int = 0,
    val usuario: String,
    val correo: String,
    val contrasena: String
)