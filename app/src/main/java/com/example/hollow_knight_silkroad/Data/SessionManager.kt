package com.example.hollow_knight_silkroad.Data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hollow_knight_silkroad.Model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {
    companion object{
        val ID_KEY = intPreferencesKey("id_usuario")
        val USER_KEY = stringPreferencesKey("nombre_usuario")
        val EMAIL_KEY = stringPreferencesKey("correo_usuario")
    }

    suspend fun saveUser(usuario: Usuario){
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = usuario.idUsuario
            preferences[USER_KEY] = usuario.usuario
            preferences[EMAIL_KEY] = usuario.correo
        }
    }

    val userFlow: Flow<Usuario?> = context.dataStore.data
        .map { preferences ->
            val id = preferences[ID_KEY]
            val nombre = preferences[USER_KEY]
            val correo = preferences[EMAIL_KEY]

            if (id != null && nombre != null && correo != null){
                Usuario(idUsuario = id, usuario = nombre, correo = correo, contrasena = "")
            } else{
                null
            }
        }

    suspend fun clearSession(){
        context.dataStore.edit {
            preferences -> preferences.clear()
        }
    }
}