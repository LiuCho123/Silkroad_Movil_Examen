package com.example.hollow_knight_silkroad.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class, Hilo::class, Respuesta::class, CheckedItem::class,
    Pregunta::class, Opcion::class],
    version = 7, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ChecklistItemDao(): ChecklistItemDao
    abstract fun hiloDao(): HiloDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun opcionDao(): OpcionDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hollow_knight_silkroad_db.db"
                )
                    .addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                prepoblarTrivia(database.preguntaDao(), database.opcionDao())
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        suspend fun prepoblarTrivia(preguntaDao: PreguntaDao, opcionDao: OpcionDao) {
            preguntaDao.insertarPreguntas(listOf(
                Pregunta(idPregunta = 1, textoPregunta = "¿Cuál es el nombre del personaje principal de Hollow Knight?")
            ))
            opcionDao.insertarOpciones(listOf(
                Opcion(preguntaId = 1, textoOpcion = "Hornet", esCorrecta = false),
                Opcion(preguntaId = 1, textoOpcion = "El Caballero", esCorrecta = true),
                Opcion(preguntaId = 1, textoOpcion = "Zote", esCorrecta = false),
                Opcion(preguntaId = 1, textoOpcion = "Quirrel", esCorrecta = false)
            ))

            preguntaDao.insertarPreguntas(listOf(
                Pregunta(idPregunta = 2, textoPregunta = "¿Qué objeto necesitas para poder 'soñar' y acceder a memorias?")
            ))
            opcionDao.insertarOpciones(listOf(
                Opcion(preguntaId = 2, textoOpcion = "Aguijón Onírico", esCorrecta = true),
                Opcion(preguntaId = 2, textoOpcion = "Capa de Ala de Polilla", esCorrecta = false),
                Opcion(preguntaId = 2, textoOpcion = "Lágrima de Isma", esCorrecta = false),
                Opcion(preguntaId = 2, textoOpcion = "Corazón de Cristal", esCorrecta = false)
            ))

            preguntaDao.insertarPreguntas(listOf(
                Pregunta(idPregunta = 3, textoPregunta = "¿En qué área comienzas el juego?")
            ))
            opcionDao.insertarOpciones(listOf(
                Opcion(preguntaId = 3, textoOpcion = "Bocasucia", esCorrecta = true),
                Opcion(preguntaId = 3, textoOpcion = "Cruces Olvidados", esCorrecta = false),
                Opcion(preguntaId = 3, textoOpcion = "Cañón Nublado", esCorrecta = false),
                Opcion(preguntaId = 3, textoOpcion = "Páramos Fúngicos", esCorrecta = false)
            ))

            println("Base de datos poblada con Trivia")
        }
    }
}