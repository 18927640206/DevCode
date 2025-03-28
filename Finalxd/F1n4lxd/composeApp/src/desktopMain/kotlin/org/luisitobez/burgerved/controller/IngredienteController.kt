package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.data.EmailSender
import org.luisitobez.burgerved.model.domain.Ingrediente


class IngredienteController(private val ingredienteDAO: IngredienteDAOImpl, private val emailSender: EmailSender) {

    fun obtenerIngredientePorId(idIng: String): Ingrediente? {
        return ingredienteDAO.getProductoById(idIng)
    }

    fun obtenerNumeroDeIngredientes(): Int {
        return ingredienteDAO.getNumeroDeIngredientes()?.numeroDeIngredientes ?: 0
    }

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
    fun generarAlertasStockAgotado(): String {
        val ingredientesAgotados = ingredienteDAO.verificarStockAgotado()

        return if (ingredientesAgotados.isNotEmpty()) {
            // Envia correo en segundo plano
            Thread {
                emailSender.enviarAlertaStock(
                    ingredientesAgotados.map { it.nombre }
                )
            }.start()

            // msensaje para la UI
            buildString {
                append("🚨 ALERTA: Ingredientes agotados:\n")
                ingredientesAgotados.forEach { append("- ${it.nombre}\n") }
            }
        } else {
            "✅ Todos los ingredientes tienen stock disponible"
        }
    }

    fun restablecerIngrediente(idIngrediente: Int, cantidad: Int) {
        if (ingredienteDAO.restablecerStock(idIngrediente, cantidad)) {
            println("Operación exitosa - ID: $idIngrediente - Cantidad: $cantidad")
        } else {
            println("Operación fallida - Revise el ID o conexión a BD")
        }
    }
}
