// app/src/main/java/com/example/huertoavance8/data/model/Usuario.kt
package com.example.huertoavance8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    // NUEVO: guardamos la URI de la foto como String (puede ser null)
    val fotoPerfil: String? = null
)
