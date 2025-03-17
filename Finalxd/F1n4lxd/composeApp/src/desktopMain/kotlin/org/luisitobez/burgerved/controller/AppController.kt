// controller/AppController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ConexionDB
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl

class AppController {
    // Inicializa la conexión a la base de datos
    private val conexionDB = ConexionDB()

    // Inicializa los DAOs
    private val productoDAO = ProductoDAOImpl(conexionDB)
    private val pedidoDAO = PedidoDAOImpl(conexionDB)
    private val ingredienteDAO = IngredienteDAOImpl(conexionDB)
    private val pedidoProductoDAO = PedidoProductoDAOImpl(conexionDB)

    // Inicializa los controladores
    val productoController = ProductoController(productoDAO, pedidoProductoDAO)
    val carritoController = CarritoController(pedidoDAO)
    val ingredienteController = IngredienteController(ingredienteDAO )


}
