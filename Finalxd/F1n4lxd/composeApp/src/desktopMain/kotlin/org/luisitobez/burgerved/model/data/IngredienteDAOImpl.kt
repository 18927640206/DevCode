package org.luisitobez.burgerved.model.data;


import org.luisitobez.burgerved.model.domain.Detalles
import org.luisitobez.burgerved.model.domain.Ingrediente
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
}