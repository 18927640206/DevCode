package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.UsuarioDAOImpl

class UsuarioController(private val usuarioDAO: UsuarioDAOImpl) {

    fun administrarSesion(correo: String, contraseña: String): Boolean {
        // Validar parámetros de entrada
        if (correo.isBlank() || contraseña.isBlank()) {
            return false
        }

        // Limpiar espacios
        val correoLimpio = correo.trim()
        val contraseñaLimpia = contraseña.trim()

        return try {
            usuarioDAO.IniciarSesion(correoLimpio, contraseñaLimpia)
        } catch (e: Exception) {
            // Loggear el error si es necesario
            false
        }
    }
}