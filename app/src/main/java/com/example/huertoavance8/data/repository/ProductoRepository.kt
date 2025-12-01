package com.example.huertoavance8.data.repository

import android.content.Context
import com.example.huertoavance8.data.database.AppDatabase
import com.example.huertoavance8.data.model.Producto

class ProductoRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val productoDao = db.productoDao()

    suspend fun getAllProductos(): List<Producto> = productoDao.getAll()

    suspend fun insertProducto(producto: Producto) = productoDao.insert(producto)

    suspend fun deleteProducto(producto: Producto) = productoDao.delete(producto)
}
