package org.luisitobez.burgerved.model.data

import org.luisitobez.burgerved.model.domain.Usuario
import java.sql.SQLException

class UsuarioDAOImpl(private val conexion: ConexionDB) {

    fun IniciarSesion(correo: String, contraseña: String): Boolean{
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

        if (usuario == null){
            return false
        }else{
            return true
        }
    }
}