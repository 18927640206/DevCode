// controller/ProductoController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.domain.PedidoProductos

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

    fun obtenerTodasBebidas(): List<Producto> {
        return productoDAO.getProductoBebida()
    }

    fun agregarProductoAPedido(pedido: Pedido, producto: Producto) {
        try {
            pedidoProductoDAO.addProducto(pedido, producto) // Llama al DAO.
        } catch (e: Exception) {
            println("Error al agregar el producto al pedido: ${e.message}")
        }
    }

    fun eliminarProductoAPedido(pedidoProductos: PedidoProductos){
        try {
            pedidoProductoDAO.borrarProducto(pedidoProducto = pedidoProductos)
        } catch (e: Exception){
            println("Error al borrar el producto del pedido: ${e.message}")
        }
    }

    fun pedirPedidoProductos(pedido: Pedido): List<PedidoProductos>{
        return pedidoProductoDAO.obtenerProductos(pedido = pedido)
    }

    fun pedirTotalAPagar(pedido: Pedido): Float{
        return pedidoProductoDAO.obtenerTotal(pedido = pedido)
    }

    fun cambiarprecioDeProducto(pedidoProductos: PedidoProductos, precioFinal: Float){
        pedidoProductoDAO.cambiarPrecio(pedidoProductos, precioFinal)
    }
    //SUGERENCIAS DE PEDIDO ***********************JOAHAN****************
    fun obtenerSugerenciasParaPedido(pedido: Pedido): List<Producto> {
        try {
            // 1. Obtener los productos más populares
            val productosPopulares = pedidoProductoDAO.obtenerProductosMasVendidos()

            // 2. Obtener los IDs de los productos del pedido actual
            val idsProductosActuales = pedidoProductoDAO.obtenerProductos(pedido).map { it.idProducto }

            // 3. Filtrar productos populares que no estén ya en el pedido
            return productosPopulares.filter { producto ->
                producto.id !in idsProductosActuales
            }.take(3) // Limitar a 3 sugerencias
        } catch (e: Exception) {
            println("Error al obtener sugerencias: ${e.message}")
            return emptyList()
        }
    }
}
