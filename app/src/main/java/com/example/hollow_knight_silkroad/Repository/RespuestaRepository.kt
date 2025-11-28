package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Respuesta
import com.example.hollow_knight_silkroad.Model.RespuestaDao
import com.example.hollow_knight_silkroad.Network.CrearRespuestaRequest
import com.example.hollow_knight_silkroad.Network.ForoApiService
import com.example.hollow_knight_silkroad.Network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Locale

class RespuestaRepository() {

    private val foroService = RetrofitClient.createService(ForoApiService::class.java, 8081)
    suspend fun getRespuestasByHiloId(hiloId: Int): List<Respuesta>{
        return try{
            val response = foroService.obtenerRespuestas(hiloId)
            if (response.isSuccessful && response.body() != null){
                response.body()!!.map{ r ->
                    Respuesta(
                        idRespuesta = r.idRespuesta,
                        hiloId = hiloId,
                        autor = r.autorRespuesta,
                        contenido = r.mensajeRespuesta,
                        fechaCreacion = parsearFecha(r.fechaRespuesta),
                        imagenUri = r.imagenBase64
                    )
                }
            } else emptyList()
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun insertarRespuesta(context: android.content.Context, respuesta: Respuesta, uriImagen: String?){
        try{
            val idUsuarioActual = UsuarioRepository.usuarioActual?.idUsuario ?: 1
            val nombreAutorActual = UsuarioRepository.usuarioActual?.usuario ?: "Anónimo"

            val imagenString = if (uriImagen != null){
                convertirUriABase64(context, uriImagen)
            } else null

            val request = CrearRespuestaRequest(
                mensaje = respuesta.contenido,
                idUsuario = idUsuarioActual,
                nombreAutor = nombreAutorActual,
                imagenBase64 = imagenString
            )

            foroService.crearRespuesta(respuesta.hiloId, request)
        } catch(e: Exception){
            println("Error enviando respuesta")
        }
    }

    suspend fun contarRespuestasByHiloId(hiloId: Int): Int{
        return getRespuestasByHiloId(hiloId).size
    }

    suspend fun actualizarRespuesta(respuesta: Respuesta){
        println("Provisorio")
    }

    suspend fun eliminarRespuesta(respuesta: Respuesta){
        println("Provisorio")
    }

    suspend fun eliminarRespuestasByHiloId(hiloId: Int){
        println("Provisorio")
    }

    private fun parsearFecha(fechaString: String?): Long{
        if(fechaString == null)
            return System.currentTimeMillis()
        return try{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.parse(fechaString)?.time ?: System.currentTimeMillis()
        } catch(e: Exception){
            System.currentTimeMillis()
        }
    }

    private fun convertirUriABase64(context: android.content.Context, uriString: String): String? {
        return try {
            val uri = android.net.Uri.parse(uriString)
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}