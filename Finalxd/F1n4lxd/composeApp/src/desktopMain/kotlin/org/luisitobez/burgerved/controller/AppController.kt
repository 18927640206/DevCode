// controller/AppController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ConexionDB
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.data.IngredientesExtraDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.data.ProductoIngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.ProductoIngredientes
import org.luisitobez.burgerved.controller.PedidoController
import  org.luisitobez.burgerved.controller.IngredienteController
import org.luisitobez.burgerved.model.data.EmailSender

class AppController {
    // Inicializa la conexión a la base de datos
    private val conexionDB = ConexionDB()

    private val emailSender = EmailSender()

    // Inicializa los DAOs
    private val productoDAO = ProductoDAOImpl(conexionDB)
    private val pedidoDAO = PedidoDAOImpl(conexionDB)
    private val ingredienteDAO = IngredienteDAOImpl(conexionDB)
    private val pedidoProductoDAO = PedidoProductoDAOImpl(conexionDB)
    private val productoIngredienteDAO = ProductoIngredienteDAOImpl(conexionDB)
    private val ingredientesExtraDAO = IngredientesExtraDAOImpl(conexionDB)


    // Inicializa los controladores

    val carritoController = CarritoController(pedidoDAO)
    val ingredienteController = IngredienteController(ingredienteDAO , emailSender)
    val productoController = ProductoController(productoDAO, pedidoProductoDAO, ingredienteDAO, ingredienteController)
    val pedidoController = PedidoController(pedidoDAO, pedidoProductoDAO)
    val productoIngredienteController = ProductoIngredienteController(productoIngredienteDAO)
    val ingredientesExtraController = IngredientesExtraController(ingredientesExtraDAO)

    fun verificarYAlertarStock(): String {
        return ingredienteController.generarAlertasStockAgotado()
    }
}
