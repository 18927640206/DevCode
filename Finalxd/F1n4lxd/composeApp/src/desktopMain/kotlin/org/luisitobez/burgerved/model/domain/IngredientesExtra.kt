package org.luisitobez.burgerved.model.domain;

data class IngredientesExtra(
    var idPedido: Int,          // Conversión a camelCase (original: id_pedido)
    var idProducto: Int,   // Conversión a camelCase (original: id_unico_producto)
    var idIngrediente: Int,     // Conversión a camelCase (original: id_ingrediente)
    var cantidad: Int
)