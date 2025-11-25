package com.example.hollow_knight_silkroad.Repository

import com.example.hollow_knight_silkroad.Model.Hilo
import com.example.hollow_knight_silkroad.Model.HiloDao

class HiloRepository(private val hiloDao: HiloDao) {

    suspend fun getAllHilos(): List<Hilo>{
        return hiloDao.getAllHilos()
    }

    suspend fun insertarHilo(hilo: Hilo){
        hiloDao.insertarHilo(hilo)
    }

    suspend fun getHiloById(id: Int): Hilo?{
        return hiloDao.getHiloById(id)
    }

    suspend fun eliminarHilo(hilo: Hilo){
        hiloDao.eliminarHilo(hilo)
    }

    suspend fun actualizarHilo(hilo: Hilo){
        hiloDao.actualizarHilo(hilo)
    }
}