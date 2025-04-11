package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.Ingrediente

class IngredienteController(private val ingredienteDAO: IngredienteDAOImpl) {

    fun obtenerTodosIngrediente(): List<Ingrediente>{
        val numero = ingredienteDAO.getNumeroDeIngredientes()?.numeroDeIngredientes ?: 0
        val Ingredientes = mutableListOf<Ingrediente>()

        for (i in 1..numero){
            val ingrediente = ingredienteDAO.getProductoById("$i")
            if (ingrediente != null){
                Ingredientes.add(ingrediente)
            }
        }


        return Ingredientes
    }
}