package org.luisitobez.burgerved.model.data


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

    fun setStock(id: Int, cantidad: Int) {
        val sql = "UPDATE Ingredientes SET stock = ? WHERE id_ingrediente = ?"

        try {
            conexion.obtenerConexion()?.use { conn ->
                conn.prepareStatement(sql).use { ps ->
                    ps.setInt(1, cantidad)
                    ps.setInt(2, id)
                    ps.executeUpdate()
                }
            }
        } catch (ex: SQLException) {
            println("Error al actualizar el stock: ${ex.message}")
            ex.printStackTrace()
            throw ex // Opcional: relanzar la excepción para manejo superior
        }
    }
}