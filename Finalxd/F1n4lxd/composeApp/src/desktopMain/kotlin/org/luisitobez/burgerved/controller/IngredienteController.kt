package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.Ingrediente

class IngredienteController(private val ingredienteDAO: IngredienteDAOImpl) {

    fun obtenerIngredientePorId(idIng: String): Ingrediente? {
        return ingredienteDAO.getProductoById(idIng)
    }

    fun obtenerNumeroDeIngredientes(): Int {
        return ingredienteDAO.getNumeroDeIngredientes()?.numeroDeIngredientes ?: 0
    }
}