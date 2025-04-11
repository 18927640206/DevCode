package org.luisitobez.burgerved.model.data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConexionDB {

    private var conexion: Connection? = null

    companion object {
        const val URL = "jdbc:mysql://localhost/BurgerVend"
        const val USER = "root"
        const val PASSWORD = ""
        const val DRIVER = "com.mysql.cj.jdbc.Driver"
    }

    fun obtenerConexion(): Connection? {
        conexion = null
        try { // Se carga el driver JDBC
            Class.forName(DRIVER)
        } catch (e: Exception) {
            println("No se pudo cargar el driver JDBC")
        }

        try { // Se establece la conexión con la base de datos
            conexion = DriverManager.getConnection("$URL?user=$USER&password=$PASSWORD")
            println("Conexión completada con burgervend.")
        } catch (ex: SQLException) {
            println("No hay conexión con la base de datos.")
            println("SQLException: ${ex.message}")
            println("SQLState: ${ex.sqlState}")
            println("Vendor Error: ${ex.errorCode}")
        }

        return conexion
    }
}


