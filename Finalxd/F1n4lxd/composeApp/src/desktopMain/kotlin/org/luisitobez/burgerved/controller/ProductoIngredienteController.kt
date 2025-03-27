package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ProductoIngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.Ingrediente
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.model.domain.ProductoIngredientes

class ProductoIngredienteController(private val productoIngredienteDAO: ProductoIngredienteDAOImpl) {

    fun obtenerIngredientesDeProducto(producto: Producto): List<ProductoIngredientes>? {
        return productoIngredienteDAO.obtenerIngredientes(producto)
    }
}