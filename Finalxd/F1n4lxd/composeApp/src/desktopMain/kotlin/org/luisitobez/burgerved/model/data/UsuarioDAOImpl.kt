package org.luisitobez.burgerved.model.data

import org.luisitobez.burgerved.model.domain.Usuario
import java.sql.SQLException

/**
 * Implementación del DAO (Data Access Object) para la entidad Usuario.
 * Proporciona operaciones de acceso a datos relacionadas con los usuarios,
 * específicamente para el inicio de sesión de administradores.
 *
 * @property conexion Objeto de conexión a la base de datos que se utilizará
 *                   para ejecutar las consultas SQL.
 */
class UsuarioDAOImpl(private val conexion: ConexionDB) {

    /**
     * Verifica las credenciales de un administrador para iniciar sesión.
     *
     * @param correo Correo electrónico del administrador.
     * @param contraseña Contraseña del administrador.
     * @return `true` si las credenciales son válidas y existe un administrador
     *         con esos datos, `false` en caso contrario o si ocurre algún error.
     *
     * @note Este método realiza una consulta a la tabla 'Administrador' para
     *       verificar las credenciales. Maneja posibles excepciones SQL y
     *       devuelve false en caso de error.
     * @warning El mensaje de error impreso en consola hace referencia a "producto ingrediente",
     *          lo cual podría ser un error de copia y pega y debería corregirse.
     */
    fun IniciarSesion(correo: String, contraseña: String): Boolean {
        val sql = "SELECT * FROM Administrador WHERE correo = ? AND contraseña = ?"
        var usuario: Usuario? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setString(1, correo)
                    consulta.setString(2, contraseña)
                    consulta.executeQuery().use { resultado ->
                        if (resultado.next()) {
                            usuario = Usuario(
                                id = resultado.getInt("id_admin"),
                                nombre = resultado.getString("nombre"),
                                correo = resultado.getString("correo"),
                                contraseña = resultado.getString("contraseña")
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener el producto ingrediente: ${ex.message}")
            ex.printStackTrace()
        }

        return usuario != null
    }
}