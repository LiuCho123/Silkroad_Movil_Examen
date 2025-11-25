package com.example.hollow_knight_silkroad.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "respuesta",
    foreignKeys = [ForeignKey(
        entity = Hilo::class,
        parentColumns = ["idHilo"],
        childColumns = ["hiloId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["hiloId"])]
)
data class Respuesta(
    @PrimaryKey(autoGenerate = true)
    val idRespuesta: Int = 0,
    val hiloId: Int,
    val autor: String,
    val contenido: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val imagenUri: String? = null
)