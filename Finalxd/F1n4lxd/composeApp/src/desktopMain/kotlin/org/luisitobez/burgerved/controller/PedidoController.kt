package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProgramadoDAOImpl
import org.luisitobez.burgerved.model.domain.EstadoProgramado
import org.luisitobez.burgerved.model.domain.Pedido
import java.time.LocalDateTime

class PedidoController(
    private val pedidoDAOImpl: PedidoDAOImpl,
    private val pedidoProgramadoDAO: PedidoProgramadoDAOImpl
) {

    fun agregarPedido() : Pedido? {
        return pedidoDAOImpl.addPedido()
    }

    fun programarPedido(pedido: Pedido, horaRecoger: LocalDateTime): Pedido {
        val horaExpirado = horaRecoger.plusMinutes(30)
        val codigo = generarCodigo()

        val pedidoAlmacenado = pedido.copy(
            pedidoProgramado = true,
            horaRecoger = horaRecoger,
            codigoRecoger = codigo,
            estadoProgramado = EstadoProgramado.PENDIENTE,
            horaExpirado = horaExpirado
        )

        if (pedidoProgramadoDAO.guardarPedido(pedidoAlmacenado)) {
            return pedidoAlmacenado
        }
        throw IllegalStateException("No se pudo programar el pedido") // Fixed typo ("peiddo" → "pedido")
    }

    private fun generarCodigo(): String {
        val letras = "ABCDEFGHJKLMNPQRSTUVWXYZ"
        val numeros = "23456789"

        val parteLetras = (1..3).map { letras.random() }.joinToString("")
        val parteNumeros = (1..3).map { numeros.random() }.joinToString("")

        return parteLetras + parteNumeros
    }

    fun verificarCodigo(codigo: String): Pedido? {
        return try {
            pedidoProgramadoDAO.recogerPedido()
                .firstOrNull {
                    it.codigoRecoger?.equals(codigo, ignoreCase = true) == true &&
                            it.estadoProgramado == EstadoProgramado.PENDIENTE
                }
        } catch (e: Exception) {
            println("Error al verificar código: ${e.message}")
            null
        }
    }

    fun marcarPedidoEntregado(pedido: Pedido) {
        try {
            val pedidoActualizado = pedido.copy(
                estadoProgramado = EstadoProgramado.ENTREGADO
            )
            if (!pedidoProgramadoDAO.guardarPedido(pedidoActualizado)) {
                throw IllegalStateException("No se pudo actualizar el estado del pedido")
            }
        } catch (e: Exception) {
            println("Error al marcar pedido como entregado: ${e.message}")
            throw e
        }
    }
}