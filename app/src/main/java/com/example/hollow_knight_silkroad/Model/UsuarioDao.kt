package com.example.hollow_knight_silkroad.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE correo = :email LIMIT 1")
    suspend fun findUsuarioByEmail(email:String): Usuario?

    @Query("SELECT * FROM usuario WHERE usuario = :username LIMIT 1")
    suspend fun findUsuarioByUsername(username: String): Usuario?

    @Query("SELECT * FROM usuario WHERE (usuario = :identifier OR correo = :identifier) AND contrasena = :password LIMIT 1")
    suspend fun findUsuarioByIdentifierAndPassword(identifier: String, password: String): Usuario?

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDER BY idUsuario DESC")
    suspend fun getAllUsuarios(): List<Usuario>
}