package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.UsuarioDAOImpl

/**
 * Controlador para la gestión de operaciones relacionadas con usuarios.
 * Esta clase proporciona métodos para administrar la sesión de un usuario.
 *
 * @property usuarioDAO Implementación del DAO (Data Access Object) para interactuar
 *                      con la capa de datos de usuarios.
 */
class UsuarioController(private val usuarioDAO: UsuarioDAOImpl) {

    /**
     * Administra el proceso de inicio de sesión de un usuario.
     * Valida las credenciales proporcionadas y delega la autenticación al DAO.
     *
     * @param correo Correo electrónico del usuario.
     * @param contraseña Contraseña del usuario.
     * @return `true` si las credenciales son válidas y el inicio de sesión es exitoso,
     *         `false` en caso contrario o si ocurre algún error.
     *
     * @note Este método realiza validaciones básicas de entrada (campos no vacíos)
     *       y limpia los espacios en blanco antes de procesar las credenciales.
     */
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
            false
        }
    }
}