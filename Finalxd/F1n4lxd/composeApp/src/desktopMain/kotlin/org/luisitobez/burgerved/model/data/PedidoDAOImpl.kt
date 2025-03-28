package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Pedido
import java.sql.SQLException
import java.sql.Statement

class PedidoDAOImpl (private val conexion : ConexionDB){
    //private val conexion = ConexionDB()

    fun getPedido(): Pedido? {
        val sql = "SELECT * FROM Pedido ORDER BY id_pedido DESC LIMIT 1;"
        var pedido: Pedido? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.executeQuery().use { resultado ->
                        if (resultado.next()) {
                            pedido = Pedido(
                                id = resultado.getInt("id_pedido"),
                                estado = resultado.getString("estado"),
                                metodo_pago = resultado.getString("metodo_pago"),
                                total_pago = resultado.getFloat("total_pago")
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener el pedido: ${ex.message}")
            ex.printStackTrace()
        }
        return pedido
    }

    fun addPedido(): Pedido {
        // Crear un pedido con valores iniciales

        val pedido = Pedido(id = 0, estado = "activo", metodo_pago = "no definido", total_pago = 0.0f, descuento = 0f, montoAhorrado = 0f)

        val sql = "INSERT INTO Pedido (estado, metodo_pago, total_pago, descuento, monto_ahorrado) VALUES (?, ?, ?, ?, ?)"
      
        var generatedId: Int? = null

        // Obtener la conexión una sola vez
        val conn = conexion.obtenerConexion()
        if (conn == null) {
            println("Error: No se pudo obtener la conexión a la base de datos.")
            return pedido
        }

        try {
            conn.autoCommit = false // Desactivar autocommit

            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { consulta ->
                consulta.setString(1, pedido.estado)
                consulta.setString(2, pedido.metodo_pago)
                consulta.setFloat(3, pedido.total_pago)
                consulta.setFloat(4, pedido.descuento)
                consulta.setFloat(5, pedido.montoAhorrado)
                
                val rowsAffected = consulta.executeUpdate()

                if (rowsAffected > 0) {
                    // Recuperar el ID generado
                    consulta.generatedKeys.use { keys ->
                        if (keys.next()) {
                            generatedId = keys.getInt(1)
                            println("Pedido guardado exitosamente. ID: $generatedId")
                        }
                    }
                    conn.commit() // Confirmar la transacción
                } else {
                    conn.rollback() // Revertir la transacción
                    println("No se pudo guardar el pedido.")
                }
            }
        } catch (ex: SQLException) {
            conn.rollback() // Revertir en caso de error
            println("Error de base de datos al guardar el pedido: ${ex.message}")
            ex.printStackTrace()
        } finally {
            conn.autoCommit = true // Restaurar autocommit
            conn.close() // Cerrar la conexión
        }

        // Actualizar el pedido con el ID generado
        return if (generatedId != null) {
            pedido.copy(id = generatedId!!)
        } else {
            println("No se generó un ID para el pedido.")
            pedido
        }
    }

    fun borrarPedido(pedido: Pedido) {
        val sql = "DELETE FROM Pedido WHERE id_pedido = ?"
        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedido.id)
                    val rowsAffected = consulta.executeUpdate()
                    println(if (rowsAffected > 0) "Pedido borrado exitosamente." else "No se pudo borrar el pedido.")
                }
            }
        } catch (ex: SQLException) {
            println("Error al borrar el pedido: ${ex.message}")
            ex.printStackTrace()
        }
    }

    // PedidoDAOImpl.kt
    fun updatePedido(pedido: Pedido, descuento: Float, montoAhorrado: Float) {
        val sql = """
        UPDATE Pedido SET 
        estado = ?, 
        metodo_pago = ?, 
        total_pago = ?,
        descuento = ?,
        monto_ahorrado = ?
        WHERE id_pedido = ?
    """.trimIndent()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setString(1, pedido.estado)
                    consulta.setString(2, pedido.metodo_pago)
                    consulta.setFloat(3, pedido.total_pago)
                    consulta.setFloat(4, descuento)
                    consulta.setFloat(5, montoAhorrado)
                    consulta.setInt(6, pedido.id)


                    val rowsAffected = consulta.executeUpdate()
                    println(if (rowsAffected > 0) "Pedido actualizado exitosamente." else "No se pudo actualizar el pedido.")
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el pedido: ${ex.message}")
            ex.printStackTrace()
        }
    }

    fun actualizarEstadoPedido(idPedido: Int, estado: String, metodoPago: String) {
        val sql = "UPDATE Pedido SET estado = ?, metodo_pago = ? WHERE id_pedido = ?"
        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, estado)
                    ps.setString(2, metodoPago)
                    ps.setInt(3, idPedido)
                    ps.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el estado del pedido: ${ex.message}")
            ex.printStackTrace()

        }
    }

    fun actualizarCostoPedido(idPedido: Int, costo: Float) {
        val sql = "UPDATE Pedido SET total_pago = ? WHERE id_pedido = ?"
        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setFloat(1, costo)
                    ps.setInt(2, idPedido)
                    ps.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el costo del pedido: ${ex.message}")
            ex.printStackTrace()

        }
    }
}
