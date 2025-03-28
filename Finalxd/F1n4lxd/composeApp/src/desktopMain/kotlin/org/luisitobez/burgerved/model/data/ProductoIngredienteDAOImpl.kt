package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Ingrediente
import org.luisitobez.burgerved.model.domain.PedidoProductos
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.model.domain.ProductoIngredientes
import java.sql.SQLException

class ProductoIngredienteDAOImpl(private val conexion: ConexionDB) {


    fun getProductoIngredienteById(idProducto: String, idIngrediente: String): ProductoIngredientes? {
        val sql = "SELECT * FROM Producto_Ingredientes WHERE id_producto = ? AND id_ingrediente = ?"
        var productoIngredientes: ProductoIngredientes? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setString(1, idProducto)
                consulta.setString(2, idIngrediente)
                consulta.executeQuery().use { resultado ->
                    if (resultado.next()) {
                        productoIngredientes = ProductoIngredientes(
                                idProducto = resultado.getInt("id_producto"),
                                idIngrediente = resultado.getInt("id_ingrediente"),
                                cantidad = resultado.getInt("cantidad")
                        )
                    }
                }
            }
            }
        } catch (ex: SQLException) {
            println("Error al obtener el producto ingrediente: ${ex.message}")
            ex.printStackTrace()
        }
        return productoIngredientes
    }

    fun obtenerIngredientes(producto: Producto): List<ProductoIngredientes>? {
        val ingredientes = mutableListOf<ProductoIngredientes>()
        val sql = "SELECT * FROM Producto_Ingredientes WHERE id_producto = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, producto.id) // Establecer el parámetro de la consulta

                    consulta.executeQuery().use { resultado ->
                        while (resultado.next()) {
                            val productoIngrediente = ProductoIngredientes(
                                idProducto = resultado.getInt("id_producto"),
                                idIngrediente = resultado.getInt("id_ingrediente"),
                                cantidad = resultado.getInt("cantidad")
                            )
                            ingredientes.add(productoIngrediente)
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener los ingredientes del producto: ${ex.message}")
            ex.printStackTrace()
        }
        return ingredientes
    }
}
