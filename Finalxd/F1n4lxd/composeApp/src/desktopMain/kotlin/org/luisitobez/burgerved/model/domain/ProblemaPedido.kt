package org.luisitobez.burgerved.model.data
//package org.luisitobez.burgerved.model.domain

enum class ProblemaPedido(val descripcion: String) {
    NO_ENTREGADO("Pedido no entregado"),
    INCOMPLETO("Pedido incompleto"),
    INCORRECTO("Producto incorrecto o en mal estado"),
    COBRO("Problemas con el cobro"),
    OTRO("Otro problema")
}
