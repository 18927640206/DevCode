package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido

class PedidoController(private val pedidoDAOImpl: PedidoDAOImpl) {

    fun agregarPedido() : Pedido? {
        return pedidoDAOImpl.addPedido()
    }

}