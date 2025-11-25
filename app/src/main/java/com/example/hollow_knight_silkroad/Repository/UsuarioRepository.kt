package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Model.UsuarioDao

class UsuarioRepository(private val usuarioDao: UsuarioDao){
    suspend fun addUsuario(nuevoUsuario: Usuario): Boolean{
        val existeEmail = usuarioDao.findUsuarioByEmail(nuevoUsuario.correo) != null
        if (existeEmail){
            println("Error al registrar: El correo '${nuevoUsuario.correo}' ya está en uso")
            return false
        }

        val existeUsuario = usuarioDao.findUsuarioByUsername(nuevoUsuario.usuario) != null
        if (existeUsuario){
            println("Error al registrar: El usuario '${nuevoUsuario.usuario}' ya está en uso")
            return false
        }


        usuarioDao.insertarUsuario(nuevoUsuario.copy(idUsuario = 0))
        println("Usuario $nuevoUsuario insertado con éxito en la base de datos")
        return true
    }

    suspend fun findUsuarioByEmail(email:String): Usuario?{
        return usuarioDao.findUsuarioByEmail(email)
    }

    suspend fun findUsuarioByIdentifier(identifier: String, contrasena: String): Usuario?{
        return usuarioDao.findUsuarioByIdentifierAndPassword(identifier, contrasena)
    }

    suspend fun updatePasswordByEmail(email: String, nuevaContrasena: String): Boolean{
        val usuario = usuarioDao.findUsuarioByEmail(email)
        return if (usuario != null){
            val usuarioActualizado = usuario.copy(contrasena = nuevaContrasena)
            usuarioDao.actualizarUsuario(usuarioActualizado)
            println("Contraseña actualizada en BD para $email")
            true
        } else{
            println("No se pudo encontrar el email $email para actualizar la contraseña")
            false
        }
    }

    suspend fun getAllUsuarios(): List<Usuario>{
        return usuarioDao.getAllUsuarios()
    }
}