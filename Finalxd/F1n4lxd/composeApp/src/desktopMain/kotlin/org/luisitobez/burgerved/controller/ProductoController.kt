// controller/ProductoController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl

class ProductoController(private val productoDAO: ProductoDAOImpl, private val pedidoProductoDAO: PedidoProductoDAOImpl) {

    fun obtenerProductoPorId(id: String): Producto? {
        return productoDAO.getProductoById(id)
    }

    fun obtenerTodosProductos(): List<Producto> {
        val numeroDeProductos = productoDAO.getNumeroDeProductos()?.numeroDeProductos ?: 0
        val productos = mutableListOf<Producto>()

        for (i in 1..numeroDeProductos) {
            val producto = productoDAO.getProductoById("$i")
            if (producto != null) {
                productos.add(producto)
            }
        }

        return productos
    }
    fun agregarProductoAPedido(pedido: Pedido, producto: Producto) {
        try {
            pedidoProductoDAO.addProducto(pedido, producto, 1) // Llama al DAO.
        } catch (e: Exception) {
            println("Error al agregar el producto al pedido: ${e.message}")
        }
    }
}