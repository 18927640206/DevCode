// controller/ProductoController.kt
package org.luisitobez.burgerved.controller

import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.domain.PedidoProductos


class ProductoController(
    private val productoDAO: ProductoDAOImpl,
    private val pedidoProductoDAO: PedidoProductoDAOImpl,
    private val ingredienteDAO : IngredienteDAOImpl,
    private val ingredienteController: IngredienteController ) {

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

    fun agregarProductoAPedido(pedido: Pedido, producto: Producto, contador: Int) {
        try {
            pedidoProductoDAO.addProducto(pedido, producto, contador) // Llama al DAO.
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

    fun procesarVenta(productoId: Int, cantidad: Int): Boolean{
        val ingredientes = ingredienteDAO.obtenerIngredientesDeProducto(productoId)
        if (ingredientes.isEmpty()) return false

        var exito = true
        for ((idIngrediente, cantidadRequerida) in ingredientes) {
            val totalDescontar = cantidadRequerida * cantidad
            if (!ingredienteDAO.descontarStock(idIngrediente, totalDescontar)) {
                exito = false
                println("Error al descontar ingrediente $idIngrediente")
            }
        }
        if(exito){
            ingredienteController.generarAlertasStockAgotado()
        }
        return exito
    }

}
