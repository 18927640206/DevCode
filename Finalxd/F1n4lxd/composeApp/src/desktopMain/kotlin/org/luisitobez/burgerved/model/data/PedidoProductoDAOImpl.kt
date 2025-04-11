package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.PedidoProductos
import org.luisitobez.burgerved.model.domain.Producto
import java.sql.SQLException

class PedidoProductoDAOImpl ( private val conexion : ConexionDB) {
    //private val conexion = ConexionDB()

    fun getPedidoProductoById(
        pedido: Pedido,
        producto: Producto,
        contadorDeProducto: Int
    ): PedidoProductos? {
        val sql =
            "SELECT * FROM Pedido_Detalles WHERE id_pedido = ? AND id_producto = ? AND id_modificacion = ?"
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

    fun addProducto(pedido: Pedido, producto: Producto) {
        val sql =
            "INSERT INTO Pedido_Detalles (id_pedido, id_producto, id_modificacion, precio_unitario) VALUES (?, ?, ?, ?)"
        val hola = obtenerUltimoPedidoDeProducto(pedido) + 1

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)
                    consulta.setInt(2, producto.id)
                    consulta.setInt(3, hola)
                    consulta.setFloat(4, producto.precio)

                    val rowsAffected = consulta.executeUpdate()
                    if (rowsAffected > 0) {
                        //logger.info("Producto guardado exitosamente: Pedido ID=${pedido.id}, Producto ID=${producto.id}")
                    } else {
                        //logger.warn("No se pudo guardar el Producto: Pedido ID=${pedido.id}, Producto ID=${producto.id}")
                    }
                }
            } ?: throw SQLException("No se pudo obtener una conexión a la base de datos.")
        } catch (ex: SQLException) {
            //logger.error("Error al guardar el producto: Pedido ID=${pedido.id}, Producto ID=${producto.id}", ex)
            throw ex
        }
    }

    fun obtenerUltimoPedidoDeProducto(pedido: Pedido): Int {
        val sql =
            "SELECT MAX(id_modificacion) AS maximiliano FROM Pedido_Detalles WHERE id_pedido = ?"
        var numeroMasAlto = 0

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)
                    val resultSet = consulta.executeQuery()  // Ejecutar la consulta

                    // Procesar el resultado
                    if (resultSet.next()) {
                        numeroMasAlto = resultSet.getInt("maximiliano") ?: 0
                    }
                }
            } ?: throw SQLException("No se pudo obtener una conexión a la base de datos.")
        } catch (ex: SQLException) {
            //logger.error("Error al guardar el producto: Pedido ID=${pedido.id}, Producto ID=${producto.id}", ex)
            throw ex
        }
        return numeroMasAlto
    }

    fun borrarProducto(pedidoProducto: PedidoProductos) {
        val sql =
            "DELETE FROM Pedido_Detalles WHERE id_pedido = ? AND id_producto = ? AND id_modificacion = ?"

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

    fun obtenerProductos(pedido: Pedido): List<PedidoProductos> {
        val pedidoProducto = mutableListOf<PedidoProductos>()
        val sql = "SELECT * FROM Pedido_Detalles WHERE id_pedido = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)

                    consulta.executeQuery().use { resultado ->
                        while (resultado.next()) {
                            val producto = PedidoProductos(
                                idPedido = resultado.getInt("id_pedido"),
                                idProducto = resultado.getInt("id_producto"),
                                idModificacion = resultado.getInt("id_modificacion"),
                                precioUnitario = resultado.getFloat("precio_unitario")
                            )
                            pedidoProducto.add(producto)
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener los productos del pedido: ${ex.message}")
            ex.printStackTrace()
        }

        return pedidoProducto
    }


    fun obtenerTotal(pedido: Pedido): Float {
        var total = 0.0f
        val sql =
            "SELECT SUM(precio_unitario) AS total_pedido FROM Pedido_Detalles WHERE id_pedido = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)  // Asignar el id_pedido al parámetro
                    val resultSet = consulta.executeQuery()  // Ejecutar la consulta

                    // Procesar el resultado
                    if (resultSet.next()) {
                        total = resultSet.getFloat("total_pedido")
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener el total del pedido: ${ex.message}")
            ex.printStackTrace()

        }

        return total
    }

    fun cambiarPrecio(pedidoProducto: PedidoProductos, precioFinal: Float) {
        val sql = "UPDATE Pedido_Detalles SET precio_unitario = ? WHERE id_modificacion = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.autoCommit = false // Desactivar autocommit para manejar transacciones

                conn.prepareStatement(sql).use { ps ->
                    // Establecer los parámetros de la consulta
                    ps.setFloat(1, precioFinal)
                    ps.setInt(2, pedidoProducto.idModificacion)

                    // Ejecutar la consulta
                    val filasAfectadas = ps.executeUpdate()

                    if (filasAfectadas > 0) {
                        conn.commit() // Confirmar la transacción
                        println("Precio actualizado correctamente.")
                    } else {
                        conn.rollback() // Revertir la transacción si no se afectaron filas
                        println("No se encontró el pedido_detalles con ID: ${pedidoProducto.idModificacion}")
                    }
                }
            }
        } catch (ex: SQLException) {
            conexion.obtenerConexion()?.rollback() // Revertir en caso de error
            println("Error al actualizar el precio: ${ex.message}")
            ex.printStackTrace()
        } finally {
            conexion.obtenerConexion()?.autoCommit = true // Restaurar autocommit
        }
    }

    fun obtenerProductosMasVendidos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val query = """
        SELECT p.id_producto AS id, p.nombre, p.descripcion AS detalles, 
               p.precio_base AS precio, p.categoria, p.imagen
        FROM Pedido_Detalles pd
        JOIN Producto p ON pd.id_producto = p.id_producto
        GROUP BY pd.id_producto
        ORDER BY COUNT(pd.id_producto) DESC
        LIMIT 5
    """

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(query).use { stmt ->
                    stmt.executeQuery().use { rs ->
                        while (rs.next()) {
                            val producto = Producto(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                detalles = rs.getString("detalles"),
                                precio = rs.getFloat("precio"),
                                categoria = rs.getString("categoria"),
                                imagen = rs.getString("imagen")
                            )
                            productos.add(producto)
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener productos más vendidos: ${ex.message}")
            ex.printStackTrace()
        }

        return productos
    }
}

