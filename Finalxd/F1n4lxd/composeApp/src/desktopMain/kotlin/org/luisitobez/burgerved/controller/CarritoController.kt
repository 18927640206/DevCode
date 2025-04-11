// controller/CarritoController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido

class CarritoController(
    private val pedidoDAO: PedidoDAOImpl
) {

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

    fun aplicarDescuento(pedido: Pedido, contador: Int): Pedido {
        val descuento = when {
            contador == 2 -> 0.15f // 15% de descuento
            contador >= 3 -> 0.20f // 20% de descuento
            else -> 0f
        }

        val montoAhorrado = pedido.total_pago * descuento
        val totalConDescuento = pedido.total_pago - montoAhorrado

        pedido.descuento = descuento
        pedido.montoAhorrado = montoAhorrado
        pedido.total_pago = totalConDescuento

        pedidoDAO.updatePedido(pedido, descuento, montoAhorrado)
        return pedido
    }

    fun notificarDescuento(cantidadProductos: Int): String {
        return when {
            cantidadProductos == 0 -> ""
            cantidadProductos == 1 -> "¡OFERTA! En la compra de 2 productos, obtienes un 15% de descuento."
            cantidadProductos == 2 -> "En la compra de 3 o más productos, obtienes un 20% de descuento."
            else -> ""
        }
    }
}