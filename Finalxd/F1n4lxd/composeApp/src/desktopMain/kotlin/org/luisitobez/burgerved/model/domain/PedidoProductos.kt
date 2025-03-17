package org.luisitobez.burgerved.model.domain;

data class PedidoProductos(
    var idPedido: Int,
    var idProducto: Int,
    var idModificacion: Int,
    var precioUnitario: Float // Tipo corregido a Float
)