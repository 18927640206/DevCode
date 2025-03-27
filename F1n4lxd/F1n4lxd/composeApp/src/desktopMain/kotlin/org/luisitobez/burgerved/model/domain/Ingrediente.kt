package org.luisitobez.burgerved.model.domain;

data class Ingrediente(
    var idIng: Int,      // Conversión a camelCase (original: id_ing)
    var nombre: String,
    var precio: Float,
    var stock: Int
)