package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.PedidoProductos
import org.luisitobez.burgerved.model.domain.Producto
import java.sql.SQLException

class PedidoProductoDAOImpl ( private val conexion : ConexionDB){
    //private val conexion = ConexionDB()

    fun getPedidoProductoById(pedido: Pedido, producto: Producto, contadorDeProducto: Int): PedidoProductos? {
        val sql = "SELECT * FROM pedido_detalles WHERE id_pedido = ? AND id_producto = ? AND id_modificacion = ?"
        var pedidoProducto: PedidoProductos? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)
                consulta.setInt(2, producto.id)
                consulta.setInt(3, contadorDeProducto)

                consulta.executeQuery().use { resultado ->
                    if (resultado.next()) {
                        pedidoProducto = PedidoProductos(
                                idPedido = resultado.getInt("id_pedido"),
                                idProducto = resultado.getInt("id_producto"),
                                idModificacion = resultado.getInt("id_modificacion"),
                                precioUnitario = resultado.getFloat("precio_unitario")
                        )
                    }
                }
            }
            }
        } catch (ex: SQLException) {
            println("No se encontró nada")
            ex.printStackTrace()
        }
        return pedidoProducto
    }

    fun addProducto(pedido: Pedido, producto: Producto, contadorDeProductos: Int) {
        val sql = "INSERT INTO pedido_detalles (id_pedido, id_producto, id_modificacion, precio_unitario) VALUES (?,?,?,?)"

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)
                consulta.setInt(2, producto.id)
                consulta.setInt(3, contadorDeProductos)
                consulta.setFloat(4, producto.precio)

                val rowsAffected = consulta.executeUpdate()
                println(if (rowsAffected > 0) "Producto guardado exitosamente." else "No se pudo guardar el Producto.")
            }
            }
        } catch (ex: SQLException) {
            println("Error al guardar el producto.")
            ex.printStackTrace()
        }
    }

    fun borrarProducto(pedidoProducto: PedidoProductos) {
        val sql = "DELETE FROM pedido_detalles WHERE id_pedido = ? AND id_producto = ? AND id_modificacion = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedidoProducto.idPedido)
                consulta.setInt(2, pedidoProducto.idProducto)
                consulta.setInt(3, pedidoProducto.idModificacion)

                val rowsAffected = consulta.executeUpdate()
                println(if (rowsAffected > 0) "Producto borrado exitosamente." else "No se pudo borrar el producto.")
            }
            }
        } catch (ex: SQLException) {
            println("Error al borrar el producto.")
            ex.printStackTrace()
        }
    }
}
