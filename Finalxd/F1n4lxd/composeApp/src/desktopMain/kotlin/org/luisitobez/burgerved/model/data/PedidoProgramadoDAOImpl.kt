package org.luisitobez.burgerved.model.data

import org.luisitobez.burgerved.model.domain.EstadoProgramado
import org.luisitobez.burgerved.model.domain.Pedido
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDateTime

class PedidoProgramadoDAOImpl(private val conexion: ConexionDB) {
    //private val conexion = ConexionDB()

    fun guardarPedido(pedido: Pedido): Boolean {
        val sql = """
            UPDATE Pedido SET
            pedido_programado = ?,
            hora_recoger = ?,
            codigo_recoger = ?,
            estado_programado = ?,
            hora_expirado = ?
            WHERE id_pedido = ?
        """.trimIndent()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setBoolean(1, pedido.pedidoProgramado)
                    ps.setTimestamp(2, Timestamp.valueOf(pedido.horaRecoger))
                    ps.setString(3, pedido.codigoRecoger)
                    ps.setString(4, pedido.estadoProgramado.name)
                    ps.setTimestamp(5, Timestamp.valueOf(pedido.horaExpirado))
                    ps.setInt(6, pedido.id)

                    return ps.executeUpdate() > 0
                }
            }
        } catch (ex: SQLException) {
            println("Error al programar pedido: ${ex.message}")
        }
        return false
    }

    fun recogerPedido(): List<Pedido> {
        val sql = """
            SELECT * FROM Pedido
            WHERE pedido_programado = TRUE
            AND estado_programado IN ('PENDIENTE', 'ENTREGADO', 'EXPIRADO')
            AND hora_recoger <= ?
            AND (hora_expirado IS NULL OR hora_expirado >= NOW())
        """.trimIndent()

        val pedidos = mutableListOf<Pedido>()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use {ps ->
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusHours(2)))
                    ps.executeQuery().use { rs ->
                        while (rs.next()) {
                            pedidos.add(mapRowToPedido(rs))
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener pedidos progamados: ${ex.message}")
            ex.printStackTrace()
        }
        return pedidos
    }

    private fun mapRowToPedido (rs: ResultSet): Pedido {
        val estadoStr = rs.getString("estado_programado")
        val estadoProgramado = try {
            EstadoProgramado.valueOf(estadoStr.uppercase())
        } catch (e: IllegalArgumentException) {
            EstadoProgramado.PENDIENTE
        }
        return Pedido(
            id = rs.getInt("id_pedido"),
            estado = rs.getString("estado"),
            metodo_pago = rs.getString("metodo_pago"),
            total_pago = rs.getFloat("total_pago"),
            descuento = rs.getFloat("descuento"),
            montoAhorrado = rs.getFloat("monto_ahorrado"),
            pedidoProgramado = rs.getBoolean("pedido_programado"),
            horaRecoger = rs.getTimestamp("hora_recoger")?.toLocalDateTime(),
            codigoRecoger = rs.getString("codigo_recoger"),
            estadoProgramado = estadoProgramado,
            horaExpirado = rs.getTimestamp("hora_expirado")?.toLocalDateTime()
        )
    }

    fun verificarCodigo(codigo: String): Pedido? {
        if (codigo.isBlank()) return null

        return try {
            val sql = """
            SELECT * FROM Pedido 
            WHERE codigo_recoger = ? 
            AND estado_programado = 'PENDIENTE'
            AND hora_recoger <= ?
            AND (hora_expirado IS NULL OR hora_expirado >= NOW())
            LIMIT 1
        """.trimIndent()

            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, codigo.uppercase())
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()))

                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            mapRowToPedido(rs)
                        } else {
                            null
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al verificar código: ${ex.message}")
            ex.printStackTrace()
            null
        }
    }
}