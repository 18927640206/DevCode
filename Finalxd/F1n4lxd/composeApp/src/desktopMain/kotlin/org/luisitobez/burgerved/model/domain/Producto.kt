package org.luisitobez.burgerved.model.domain;

data class Producto(
    var id: Int,
    var nombre: String,
    var detalles: String,
    var precio: Float,
    var categoria: String,
    var imagen: String
)