package org.luisitobez.burgerved.model.domain;

data class Pedido(
    var id: Int,
    var estado: String,
    var metodo_pago: String,
    var total_pago: Float,
    var descuento: Float = 0f,
    var montoAhorrado: Float = 0f
)