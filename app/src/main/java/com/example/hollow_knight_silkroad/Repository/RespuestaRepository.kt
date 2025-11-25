package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Respuesta
import com.example.hollow_knight_silkroad.Model.RespuestaDao

class RespuestaRepository(private val respuestaDao: RespuestaDao) {
    suspend fun getRespuestasByHiloId(hiloId: Int): List<Respuesta>{
        return respuestaDao.getRespuestasByHiloId(hiloId)
    }

    suspend fun insertarRespuesta(respuesta: Respuesta){
        respuestaDao.insertarRespuesta(respuesta)
    }

    suspend fun contarRespuestasByHiloId(hiloId: Int): Int{
        return respuestaDao.contarRespuestasByHiloId(hiloId)
    }

    suspend fun actualizarRespuesta(respuesta: Respuesta){
        respuestaDao.actualizarRespuesta(respuesta)
    }

    suspend fun eliminarRespuesta(respuesta: Respuesta){
        respuestaDao.eliminarRespuesta(respuesta)
    }

    suspend fun eliminarRespuestasByHiloId(hiloId: Int){
        respuestaDao.eliminarRespuestasByHiloId(hiloId)
    }
}