package org.luisitobez.burgerved.model.domain;


data class ProductoIngredientes(
    var idProducto: Int,
    var idIngrediente: Int,
    var cantidad: Int
)