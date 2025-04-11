package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.IngredientesExtraDAOImpl
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.domain.Ingrediente
import org.luisitobez.burgerved.model.domain.IngredientesExtra
import org.luisitobez.burgerved.model.domain.PedidoProductos

class IngredientesExtraController(private val ingredientesExtraDAOImpl: IngredientesExtraDAOImpl) {

    fun agregaringrediente(pedidoProductos: PedidoProductos, idIngrediente: Int, cantidad: Int){
        ingredientesExtraDAOImpl.addIngredienteExtra(pedidoProductos, idIngrediente, cantidad)
    }

    fun obtenerTodosIngrediente(pedidoProducto: PedidoProductos): List<IngredientesExtra>{
        return ingredientesExtraDAOImpl.getAllIngredientesExtra(pedidoProducto)
    }
}