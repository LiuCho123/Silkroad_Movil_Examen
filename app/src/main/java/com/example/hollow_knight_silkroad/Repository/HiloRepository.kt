package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Model.HiloDao
import com.example.hollow_knight_silkroad.Network.CrearHiloRequest
import com.example.hollow_knight_silkroad.Network.ForoApiService
import com.example.hollow_knight_silkroad.Network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Locale

class HiloRepository() {

    private val foroService = RetrofitClient.createService(ForoApiService::class.java, 8081)

    suspend fun getAllHilos(): List<Hilo>{
        return try{
            val response = foroService.obtenerTodos()
            if (response.isSuccessful && response.body() != null){
                val listaJava = response.body()!!

                listaJava.map{hiloJava ->
                    Hilo(
                        idHilo = hiloJava.idHilo,
                        titulo = hiloJava.tituloHilo,
                        contenido = hiloJava.mensajeInicialHilo,
                        autor = hiloJava.autorHilo,
                        fechaCreacion = parsearFecha(hiloJava.fechaHilo),
                        imagenUri = hiloJava.imagenBase64
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun insertarHilo(context: android.content.Context, hilo: Hilo, uriImagen: String?): Boolean{
        try{
            val idUsuarioActual = UsuarioRepository.usuarioActual?.idUsuario ?: 1
            val nombreAutorActual = UsuarioRepository.usuarioActual?.usuario ?: "Anónimo"

            val imagenString = if(uriImagen != null){
                println("📸 URI recibida en Repo: $uriImagen")
                val base64 = convertirUriABase64(context, uriImagen)
                println("📦 Base64 generado: ${base64?.take(20)}... (Largo total: ${base64?.length ?: 0})")
                base64
            } else{
                println("⚠URI es NULL en el Repo")
                null
            }

            val request = CrearHiloRequest(
                tituloHilo = hilo.titulo,
                mensaje = hilo.contenido,
                idUsuario = idUsuarioActual,
                nombreAutor = nombreAutorActual,
                imagenBase64 = imagenString
            )

            val response = foroService.crearHilo(request)
            if (response.isSuccessful){
                println("Hilo creado con éxito:${response.body()?.idHilo} ")
                return true
            } else{
                val errorBody = response.errorBody()?.string()
                println("Error creando hilo: Código ${response.code()} - $errorBody")
                return false
            }
        } catch (e: Exception){
            println("Error de conexión al crear: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    suspend fun getHiloById(id: Int): Hilo?{
        return try{
            val response = foroService.obtenerHilo(id)
            if (response.isSuccessful && response.body() != null){
                val h = response.body()!!
                Hilo(
                    idHilo = h.idHilo,
                    titulo = h.tituloHilo,
                    contenido = h.mensajeInicialHilo,
                    autor = h.autorHilo,
                    fechaCreacion = parsearFecha(h.fechaHilo),
                    imagenUri = h.imagenBase64
                )
            } else null
        } catch (e: Exception){
            null
        }
    }

    suspend fun eliminarHilo(hilo: Hilo){
        try{
            foroService.eliminarHilo(hilo.idHilo)
        } catch (e: Exception){
            println("Error eliminado el hilo")
        }
    }

    suspend fun actualizarHilo(hilo: Hilo){
        println("Temporal")
    }

    private fun parsearFecha(fechaString: String?): Long{
        if (fechaString == null)
            return System.currentTimeMillis()
        return try{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.parse(fechaString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception){
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
            } else {
                println("❌ Error: Leí 0 bytes de la imagen")
                null
            }
        } catch (e: Exception) {
            println("❌ Excepción convirtiendo imagen: ${e.message}") // <--- LOG ERROR
            e.printStackTrace()
            null
        }
    }
}

