package com.example.huertoavance8.data.repository

import android.content.Context
import com.example.huertoavance8.data.database.AppDatabase
import com.example.huertoavance8.data.model.Carrito

class CarritoRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val carritoDao = db.carritoDao()

    suspend fun getAll(): List<Carrito> = carritoDao.getAll()

    suspend fun insert(carrito: Carrito) = carritoDao.insert(carrito)

    suspend fun update(carrito: Carrito) = carritoDao.update(carrito)

    suspend fun delete(carrito: Carrito) = carritoDao.delete(carrito)

    suspend fun clearAll() = carritoDao.clearAll()
}
