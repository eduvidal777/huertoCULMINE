package com.example.huertoavance8.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huertoavance8.data.dao.CarritoDao
import com.example.huertoavance8.data.dao.ProductoDao
import com.example.huertoavance8.data.dao.UsuarioDao
import com.example.huertoavance8.data.model.Carrito
import com.example.huertoavance8.data.model.Producto
import com.example.huertoavance8.data.model.Usuario

@Database(
    entities = [Usuario::class, Producto::class, Carrito::class],
    version = 2,              // 🔴 SUBIMOS LA VERSIÓN (antes la tenías en 1)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_db"
                )
                    // ⚠️ IMPORTANTE: si cambia el esquema, borra y recrea la BD
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
