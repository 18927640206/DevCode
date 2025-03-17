package org.luisitobez.burgerved.model.data;

import org.luisitobez.burgerved.model.domain.Ingrediente
import org.luisitobez.burgerved.model.domain.IngredientesExtra
import org.luisitobez.burgerved.model.domain.PedidoProductos
import java.sql.SQLException

class IngredientesExtraDAOImpl {
    private val conexion = ConexionDB()

    fun getIngredientesExtraById(pedidoProducto: PedidoProductos, ingrediente: Ingrediente): IngredientesExtra? {
        val sql = "SELECT * FROM Ingredientes_Extra WHERE id_pedido = ? AND id_modificacion = ? AND id_ingrediente = ?"
        var ingredientesExtra: IngredientesExtra? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedidoProducto.idPedido)
                consulta.setInt(2, pedidoProducto.idModificacion)
                consulta.setInt(3, ingrediente.idIng)

                consulta.executeQuery().use { resultado ->
                    if (resultado.next()) {
                        ingredientesExtra = IngredientesExtra(
                                idPedido = resultado.getInt("id_pedido"),
                                idProducto = resultado.getInt("id_modificacion"),
                                idIngrediente = resultado.getInt("id_ingrediente"),
                                cantidad = resultado.getInt("cantidad")
                        )
                    }
                }
            }
            }
        } catch (ex: SQLException) {
            println("No se encontró nada")
            ex.printStackTrace()
        }
        return ingredientesExtra
    }

    fun addIngredienteExtra(pedidoProducto: PedidoProductos, idIngrediente: Int, cantidad: Int) {
        val sql = "INSERT INTO Ingredientes_Extra (id_pedido, id_modificacion, id_ingrediente, cantidad) VALUES (?,?,?,?)"

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, pedidoProducto.idPedido)
                consulta.setInt(2, pedidoProducto.idModificacion)
                consulta.setInt(3, idIngrediente)
                consulta.setInt(4, cantidad)

                val rowsAffected = consulta.executeUpdate()
                println(if (rowsAffected > 0) "Ingrediente extra guardado exitosamente." else "No se pudo guardar el ingrediente extra.")
            }
            }
        } catch (ex: SQLException) {
            println("Error al guardar el ingrediente.")
            ex.printStackTrace()
        }
    }

    fun updateCantidad(pedidoProducto: PedidoProductos, idIngrediente: Int, cantidad: Int) {
        val sql = "UPDATE Ingredientes_Extra SET cantidad = ? WHERE id_pedido = ? AND id_modificacion = ? AND id_ingrediente = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                    conn.prepareStatement(sql).use { consulta ->
                    consulta.setInt(1, cantidad)
                consulta.setInt(2, pedidoProducto.idPedido)
                consulta.setInt(3, pedidoProducto.idModificacion)
                consulta.setInt(4, idIngrediente)

                val rowsAffected = consulta.executeUpdate()
                println(if (rowsAffected > 0) "Cantidad de ingrediente extra actualizada exitosamente." else "No se pudo actualizar la cantidad.")
            }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar la cantidad de ingrediente extra.")
            ex.printStackTrace()
        }
    }
}
