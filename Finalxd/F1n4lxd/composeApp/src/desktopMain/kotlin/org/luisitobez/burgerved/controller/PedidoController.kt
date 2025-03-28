package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.PedidoProductos

class PedidoController(
    private val pedidoDAOImpl: PedidoDAOImpl,
    private val pedidoProductoDAO: PedidoProductoDAOImpl
) {
    fun agregarPedido(): Pedido? {
        return pedidoDAOImpl.addPedido()
    }

    fun obtenerProductosDelPedido(pedido: Pedido): List<PedidoProductos> {
        return pedidoProductoDAO.obtenerProductos(pedido)
    }
}