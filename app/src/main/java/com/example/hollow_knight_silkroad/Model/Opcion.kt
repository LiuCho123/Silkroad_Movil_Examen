package com.example.hollow_knight_silkroad.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "opcion",
    foreignKeys = [ForeignKey(
        entity = Pregunta::class,
        parentColumns = ["idPregunta"],
        childColumns = ["preguntaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["preguntaId"])]
)
data class Opcion(
    @PrimaryKey(autoGenerate = true)
    val idOpcion: Int = 0,
    val preguntaId: Int,
    val textoOpcion: String,
    val esCorrecta: Boolean = false
)