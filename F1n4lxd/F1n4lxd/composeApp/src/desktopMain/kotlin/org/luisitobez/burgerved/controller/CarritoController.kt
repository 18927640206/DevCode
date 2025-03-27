// controller/CarritoController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido

class CarritoController(private val pedidoDAO: PedidoDAOImpl) {

    fun agregarPedido(): Pedido {
        return pedidoDAO.addPedido()
    }

    fun obtenerUltimoPedido(): Pedido {
        return pedidoDAO.getPedido() ?: throw NoSuchElementException("No se encontró ningún pedido.")
    }

    fun eliminarPedido(pedido: Pedido) {
        pedidoDAO.borrarPedido(pedido)

    }


    fun actualizarPrecioPedido(pedido: Pedido, precioTotal: Float){
        pedidoDAO.actualizarCostoPedido(pedido.id, precioTotal)
    }



    fun realizarPago(pedido: Pedido, totalAmount: Float, metodoPago: String): String {
        // logica para procesar el pago (por ejemplo, actualizar el estado del pedido)
        if (pedido.estado == "Pagado") {
            throw IllegalStateException("El pedido ya ha sido pagado.")
        }
        pedidoDAO.actualizarEstadoPedido(pedido.id, "Pagado", metodoPago)
        return "Pago exitoso por $$totalAmount"
    }
}