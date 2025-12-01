// app/src/main/java/com/example/huertoavance8/viewmodel/UsuarioViewModel.kt
package com.example.huertoavance8.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertoavance8.data.model.Usuario
import com.example.huertoavance8.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(application)

    // Estado del login
    private val _loginExitoso = MutableStateFlow<Boolean?>(null)
    val loginExitoso: StateFlow<Boolean?> get() = _loginExitoso

    // Estado del registro
    private val _registroExitoso = MutableStateFlow<Boolean?>(null)
    val registroExitoso: StateFlow<Boolean?> get() = _registroExitoso

    // Usuario actual (para Home y Perfil)
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> get() = _usuarioActual

    // LOGIN
    fun loginUsuario(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val user = repository.login(correo, contrasena)
                if (user != null) {
                    _usuarioActual.value = user
                    _loginExitoso.value = true
                } else {
                    _loginExitoso.value = false
                }
            } catch (e: Exception) {
                _loginExitoso.value = false
            }
        }
    }

    // REGISTRO
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena
                )
                repository.register(nuevoUsuario)

                // opcional: loguear de inmediato al usuario recién creado
                val userGuardado = repository.login(correo, contrasena)
                _usuarioActual.value = userGuardado

                _registroExitoso.value = true
            } catch (e: Exception) {
                _registroExitoso.value = false
            }
        }
    }

    // Actualizar solo la foto de perfil
    fun actualizarFotoPerfil(uriFoto: String) {
        val usuario = _usuarioActual.value ?: return
        viewModelScope.launch {
            try {
                repository.actualizarFotoPerfil(usuario.id, uriFoto)
                _usuarioActual.value = usuario.copy(fotoPerfil = uriFoto)
            } catch (_: Exception) {
                // podrías manejar error si quieres
            }
        }
    }

    // Obtener usuario actual directamente
    fun getUsuarioActual(): Usuario? = _usuarioActual.value

    // Reset de estados
    fun resetLoginState() {
        _loginExitoso.value = null
    }

    fun resetRegistroState() {
        _registroExitoso.value = null
    }
}
