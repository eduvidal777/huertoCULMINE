// app/src/main/java/com/example/huertoavance8/data/dao/UsuarioDao.kt
package com.example.huertoavance8.data.dao

import androidx.room.*
import com.example.huertoavance8.data.model.Usuario

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Usuario?

    @Query("UPDATE usuario SET fotoPerfil = :uriFoto WHERE id = :id")
    suspend fun actualizarFotoPerfil(id: Int, uriFoto: String)
    // 🔹 NUEVO: obtener todos los usuarios
    @Query("SELECT * FROM usuario")
    suspend fun getAllUsuarios(): List<Usuario>
}

