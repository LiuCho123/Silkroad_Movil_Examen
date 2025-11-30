package com.example.hollow_knight_silkroad.Repository

import android.util.Log
import com.example.hollow_knight_silkroad.Network.SpeedrunApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpeedrunRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://www.speedrun.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeedrunApiService::class.java)

    suspend fun obtenerRecordMundial(): String{
        return try{
            val response = api.getWorldRecord()
            if (response.isSuccessful && response.body() != null){
                val run = response.body()!!.data.runs.firstOrNull()

                if (run != null){
                    val segundos = run.run.times.tiempoSegundos.toInt()
                    val minutos = segundos / 60
                    val segRestantes = segundos % 60

                    "Record Mundial Any%: ${minutos}m ${segRestantes}s"
                } else{
                    "No hay datos de speedrun"
                }
            } else{
                "Error API: ${response.code()}"
            }
        } catch (e: Exception){
            Log.e("SPEEDRUN", "Error: ${e.message}")
            "Sin conexión a Speedrun.com"

        }
    }
}