// app/src/main/java/com/example/huertoavance8/data/repository/UsuarioRepository.kt
package com.example.huertoavance8.data.repository

import android.content.Context
import androidx.room.Room
import com.example.huertoavance8.data.database.AppDatabase
import com.example.huertoavance8.data.model.Usuario

class UsuarioRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "huerto_db"
    )
        .fallbackToDestructiveMigration() // por si cambias versión más adelante
        .build()

    private val usuarioDao = db.usuarioDao()

    suspend fun login(correo: String, contrasena: String): Usuario? =
        usuarioDao.login(correo, contrasena)

    suspend fun register(usuario: Usuario) =
        usuarioDao.insert(usuario)

    suspend fun getUsuarioById(id: Int): Usuario? =
        usuarioDao.getById(id)

    suspend fun actualizarFotoPerfil(id: Int, uriFoto: String) =
        usuarioDao.actualizarFotoPerfil(id, uriFoto)

    // 🔹 NUEVO: para el Perfil, obtener todos los usuarios
    suspend fun getAll(): List<Usuario> {
        return usuarioDao.getAllUsuarios()
    }
}
