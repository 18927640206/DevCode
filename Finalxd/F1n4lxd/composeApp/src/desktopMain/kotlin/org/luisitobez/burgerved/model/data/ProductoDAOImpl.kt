package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Detalles
import org.luisitobez.burgerved.model.domain.Producto;

import java.sql.SQLException

class ProductoDAOImpl (private val conexion: ConexionDB){
    //private val conexion = ConexionDB()

   init {
        println("com.mycompany.productoDAO.ProductoDaoImpl.<init>()")
    }

    fun getProductoById(id: String): Producto? {
        val sql = "SELECT * FROM Producto WHERE id_producto = ?"
        var producto: Producto? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, id)
                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            producto = Producto(
                                id = rs.getInt("id_producto"),
                                nombre = rs.getString("nombre"),
                                detalles = rs.getString("descripcion"),
                                precio = rs.getFloat("precio_base"),
                                categoria = rs.getString("categoria"),
                                imagen = rs.getString("imagen")
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("No se encontraron productos")
            ex.printStackTrace()
        }

        return producto
    }

    fun getNumeroDeProductos(): Detalles? {
        val sql = "SELECT COUNT(*) FROM Producto"
        var detalles: Detalles? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            detalles = Detalles(
                                numeroDeProductos = rs.getInt(1),
                                numeroDeIngredientes = 0 // Valor por defecto
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener número de productos")
            ex.printStackTrace()
        }

        return detalles
    }
}