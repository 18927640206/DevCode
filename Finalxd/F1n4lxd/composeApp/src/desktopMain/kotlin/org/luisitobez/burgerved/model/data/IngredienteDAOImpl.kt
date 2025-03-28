package org.luisitobez.burgerved.model.data;


import org.luisitobez.burgerved.model.domain.Detalles
import org.luisitobez.burgerved.model.domain.Ingrediente
import org.luisitobez.burgerved.model.domain.PedidoProductos
import java.sql.SQLException


class IngredienteDAOImpl(private val conexion: ConexionDB) {
    //private val conexion = ConexionDB()

    init {
        println("com.mycompany.productoDAO.ProductoDaoImpl.<init>()")
    }

    fun getProductoById(id_ing: String): Ingrediente? {
        val sql = "SELECT * FROM Ingredientes WHERE id_ingrediente = ?"
        var ingrediente: Ingrediente? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, id_ing)
                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            ingrediente = Ingrediente(
                                idIng = rs.getInt("id_ingrediente"),
                                nombre = rs.getString("nombre"),
                                precio = rs.getFloat("precio"),
                                stock =  rs.getInt("stock")
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener ingrediente con ID: $id_ing")
            ex.printStackTrace()
        }
        return ingrediente
    }

    fun getNumeroDeIngredientes(): Detalles? {
        val sql = "SELECT COUNT(*) FROM Ingredientes"
        var detalles: Detalles? = null

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            detalles = Detalles(
                                numeroDeProductos = 0,  // Valor por defecto
                                numeroDeIngredientes = rs.getInt(1)
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al contar ingredientes")
            ex.printStackTrace()
        }
        return detalles
    }

    fun getIngredientes(): List<Ingrediente> {
        val ingredientes = mutableListOf<Ingrediente>() // Lista mutable para almacenar los ingredientes
        val sql = "SELECT * FROM Ingredientes" // Consulta SQL

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.executeQuery().use { rs ->
                        // Recorrer todas las filas del ResultSet
                        while (rs.next()) {
                            val ingrediente = Ingrediente(
                                idIng = rs.getInt("id_ingrediente"),
                                nombre = rs.getString("nombre"),
                                precio = rs.getFloat("precio"),
                                stock = rs.getInt("stock")
                            )
                            ingredientes.add(ingrediente) // Agregar el ingrediente a la lista
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener los ingredientes: ${ex.message}")
            ex.printStackTrace()
        }

        return ingredientes // Devolver la lista de ingredientes
    }
   fun descontarStock(idIngrediente: Int, cantidad: Int): Boolean {
        val sql = "UPDATE Ingredientes SET stock = stock - ? WHERE id_ingrediente = ?"
        var exito = false

       try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setInt(1, cantidad)
                    ps.setInt(2, idIngrediente)
                    val filasAfectadas = ps.executeUpdate()
                    exito = filasAfectadas > 0
                }
           }
       } catch (ex: SQLException) {
            println("Error al descontar stock del ingrediente $idIngrediente: ${ex.message}")
            ex.printStackTrace()
       }

        return exito
   }

    fun obtenerIngredientesDeProducto(idProducto: Int): Map<Int, Int> {
        val sql = "SELECT id_ingrediente, cantidad FROM Producto_Ingredientes WHERE id_producto = ?"
        val ingredientes = mutableMapOf<Int, Int>()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setInt(1, idProducto)
                    ps.executeQuery().use { rs ->
                        while (rs.next()) {
                            val idIngrediente = rs.getInt("id_ingrediente")
                            val cantidadRequerida = rs.getInt("cantidad")
                            ingredientes[idIngrediente] = cantidadRequerida
                        }
                   }
                }
            }
        } catch (ex: SQLException) {
            println("Error al obtener ingredientes del producto $idProducto: ${ex.message}")
            ex.printStackTrace()
        }

        return ingredientes
    }
    fun actualizarStock(idIngrediente: Int, cantidad: Int): Boolean {
        val sql = "UPDATE Ingredientes SET stock = ? WHERE id_ingrediente = ?"
        var exito = false

        try {
            conexion.obtenerConexion()?.use { conn -> conn.prepareStatement(sql).use { ps ->
                    ps.setInt(1, cantidad)
                    ps.setInt(2, idIngrediente)
                    ps.setInt(3, cantidad)
                    val filasAfectadas = ps.executeUpdate()
                    exito = filasAfectadas > 0
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el stock del ingrediente $idIngrediente: ${ex.message}")
            ex.printStackTrace()
        }

        return exito
    }
    fun verificarStockAgotado(): List<Ingrediente> {
        //para prueba de stock agotado
       /* return listOf(
            Ingrediente(idIng = 1, nombre = "Pan de Hamburguesa", precio = 5.0f, stock = 0),
            Ingrediente(idIng = 3, nombre = "Queso amarillo", precio = 10.0f, stock = 0)
        )*/
        val sql = "SELECT * FROM Ingredientes WHERE stock <= 0"
        val ingredientesAgotados = mutableListOf<Ingrediente>()

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.executeQuery().use { rs ->
                        while (rs.next()) {
                            ingredientesAgotados.add(
                                Ingrediente(
                                    idIng = rs.getInt("id_ingrediente"),
                                    nombre = rs.getString("nombre"),
                                    precio = rs.getFloat("precio"),
                                    stock = rs.getInt("stock")
                                )
                            )
                        }
                    }
                }
            }
        } catch (ex: SQLException) {
            println("Error al verificar stock agotado: ${ex.message}")
            ex.printStackTrace()
        }

        return ingredientesAgotados
    }

    fun restablecerStock(idIngrediente: Int, cantidad: Int): Boolean {
        return try {
            val actualizado = conexion.obtenerConexion()?.prepareStatement(
                "UPDATE Ingredientes SET stock = stock + ? WHERE id_ingrediente = ?"
            )?.use { ps ->
                ps.setInt(1, cantidad)
                ps.setInt(2, idIngrediente)
                ps.executeUpdate() > 0
            } ?: false

            if (actualizado) {
                println("Stock actualizado: +$cantidad unidades al ingrediente $idIngrediente")
            } else {
                println("Error al actualizar el ingrediente $idIngrediente")
            }
            actualizado
        } catch (ex: SQLException) {
            println("Error en BD: ${ex.message}")
            false
        }
    }
}