package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Pedido
import java.sql.SQLException

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

    fun addPedido(pedido: Pedido) {
        val sql = "INSERT INTO Pedido (estado, metodo_pago, total_pago) VALUES (?,?,?)"
        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setString(1, pedido.estado)
                    consulta.setString(2, pedido.metodo_pago)
                    consulta.setFloat(3, pedido.total_pago)
                    val rowsAffected = consulta.executeUpdate()
                    println(if (rowsAffected > 0) "Pedido guardado exitosamente." else "No se pudo guardar el pedido.")
                }
            }
        } catch (ex: SQLException) {
            println("Error al guardar el pedido: ${ex.message}")
            ex.printStackTrace()
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
    fun updatePedido(pedido: Pedido) {
        val sql = """
        UPDATE Pedido SET 
        estado = ?, 
        metodo_pago = ?, 
        total_pago = ? 
        WHERE id_pedido = ?
    """.trimIndent()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { consulta ->
                    consulta.setString(1, pedido.estado)
                    consulta.setString(2, pedido.metodo_pago)
                    consulta.setFloat(3, pedido.total_pago)
                    consulta.setInt(4, pedido.id)

                    val rowsAffected = consulta.executeUpdate()
                    println(if (rowsAffected > 0) "Pedido actualizado exitosamente." else "No se pudo actualizar el pedido.")
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el pedido: ${ex.message}")
            ex.printStackTrace()
        }
    }
    fun actualizarEstadoPedido(idPedido: Int, estado: String) {
        val sql = "UPDATE Pedido SET estado = ? WHERE id_pedido = ?"
        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, estado)
                    ps.setInt(2, idPedido)
                    ps.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el estado del pedido: ${ex.message}")
            ex.printStackTrace()

        }
    }
}
