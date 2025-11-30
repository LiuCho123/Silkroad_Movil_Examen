package com.example.hollow_knight_silkroad.Repository

import android.content.Context
import com.example.hollow_knight_silkroad.Data.SessionManager
import com.example.hollow_knight_silkroad.Model.Usuario
import com.example.hollow_knight_silkroad.Model.UsuarioDao
import com.example.hollow_knight_silkroad.Network.AuthApiService
import com.example.hollow_knight_silkroad.Network.CambioPasswordRequest
import com.example.hollow_knight_silkroad.Network.LoginRequest
import com.example.hollow_knight_silkroad.Network.RegisterRequest
import com.example.hollow_knight_silkroad.Network.RetrofitClient
import kotlinx.coroutines.flow.first
import org.intellij.lang.annotations.Identifier


class UsuarioRepository(context: Context){

    private val authService = RetrofitClient.createService(AuthApiService::class.java, 8080)

    private val sessionManager = SessionManager(context)
    companion object{
        var usuarioActual: Usuario? = null
    }

    suspend fun verificarSesionGuardad(): Boolean {
        val usuarioGuardado = sessionManager.userFlow.first()

        return if (usuarioGuardado != null){
            println("Sesion de usuario recuperada: ${usuarioGuardado.usuario}")
            usuarioActual = usuarioGuardado
            true
        } else{
            false
        }
    }

    fun obtenerIdUsuarioGuardado(): Int {
        return usuarioActual?.idUsuario ?: -1
    }

    // --- NUEVO MÉTODO AGREGADO ---
    // Este es el que necesita RankingViewModel para mostrar tu nombre real
    fun obtenerNombreUsuarioGuardado(): String? {
        return usuarioActual?.usuario
    }
    // -----------------------------

    suspend fun logout(){
        sessionManager.clearSession()
        usuarioActual = null
    }

    suspend fun findUsuarioByIdentifier(identifier: String, contrasena: String): Usuario?{
        return try{
            println("Intentando login para $identifier")
            val request = LoginRequest(login = identifier, contrasena = contrasena)

            val response = authService.login(request)

            if (response.isSuccessful && response.body() != null){
                val apiResponse = response.body()!!
                println("Login exitoso para ${apiResponse.usuario}")

                val usuarioLogueado = Usuario(
                    idUsuario = apiResponse.idUsuario,
                    usuario = apiResponse.usuario,
                    correo = apiResponse.correo,
                    contrasena = ""
                )

                usuarioActual = usuarioLogueado
                sessionManager.saveUser(usuarioLogueado)

                return usuarioLogueado
            } else {
                println("Error en Login: ${response.code()}")
                return null;
            }
        } catch (e: Exception){
            println("Error de conexíon ${e.message}")
            return null;
        }
    }
    suspend fun addUsuario(nuevoUsuario: Usuario): Boolean{
        return try{
            println("Intendo registrar en AWS: ${nuevoUsuario.usuario}")

            val request = RegisterRequest(
                usuario = nuevoUsuario.usuario,
                correo = nuevoUsuario.correo,
                contrasena = nuevoUsuario.contrasena
            )

            val response = authService.registro(request)

            if (response.isSuccessful && response.body() != null){
                println("Registo exitoso! ID: ${response.body()!!.idUsuario}")
                return true
            } else{
                println("Error en registro: ${response.code()} - ${response.errorBody()?.string()}")
                return false
            }
        } catch (e: Exception){
            println("Error de conexión: ${e.message}")
            return false;
        }
    }

    suspend fun solicitarCodigoRecuperacion(email: String): String? {
        return try{
            val response = authService.solicitarCodigo(email)

            if (response.isSuccessful && response.body() != null){
                val codigo = response.body()!!.string()
                println("Codigo Recibido")
                return codigo
            } else{
                return null
            }
        } catch (e: Exception){
            return null
        }
    }
    suspend fun updatePasswordByEmail(email: String, nuevaContrasena: String): Boolean{
        return try{
            val request = CambioPasswordRequest(correo = email, nuevaContrasena = nuevaContrasena)
            val response = authService.cambiarPassword(request)

            if (response.isSuccessful){
                return true;
            } else{
                return false;
            }
        } catch (e: Exception){
            return false;
        }
    }

    suspend fun findUsuarioByEmail(email:String): Usuario?{
        println("Funcion pa despues")
        return null;
    }

    suspend fun getAllUsuarios(): List<Usuario>{
        return emptyList()
    }
}