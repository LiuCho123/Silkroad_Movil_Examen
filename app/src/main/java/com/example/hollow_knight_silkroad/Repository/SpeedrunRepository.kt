package com.example.hollow_knight_silkroad.Repository

import android.util.Log
import com.example.hollow_knight_silkroad.Network.SpeedrunApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpeedrunRepository {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0")
                .build()
            chain.proceed(request)
        }
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://www.speedrun.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeedrunApiService::class.java)

    suspend fun obtenerRecordMundial(): String {
        return try {
            val response = api.getWorldRecord()

            if (response.isSuccessful && response.body() != null) {
                val run = response.body()!!.data.runs.firstOrNull()

                if (run != null) {
                    val segundos = run.run.times.tiempoSegundos.toInt()
                    val minutos = segundos / 60
                    val segRestantes = segundos % 60
                    val segString = if (segRestantes < 10) "0$segRestantes" else "$segRestantes"

                    "Récord Mundial Any%: ${minutos}m ${segString}s"
                } else {
                    "No hay datos de speedrun"
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("SPEEDRUN", "Error API: ${response.code()} - $errorBody")
                "Error API: ${response.code()}"
            }
        } catch (e: Exception) {
            Log.e("SPEEDRUN", "⚠Excepción: ${e.message}")
            e.printStackTrace()
            "Sin conexión a Speedrun.com"
        }
    }
}