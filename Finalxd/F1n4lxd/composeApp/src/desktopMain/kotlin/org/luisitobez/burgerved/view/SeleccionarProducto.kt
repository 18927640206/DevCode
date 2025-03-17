package org.luisitobez.burgerved.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.luisitobez.burgerved.model.data.ConexionDB
import org.luisitobez.burgerved.model.data.ProductoDAOImpl


@Composable
fun SeleccionarProducto(productId: Int) {
    val productoDAO = ProductoDAOImpl(ConexionDB())
    val producto = productoDAO.getProductoById("$productId")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        /*Image(
            painter = painterResource("rb_2151137700.png"), // Reemplaza con la ruta correcta
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .size(90.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )*/

        // Detalles del producto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (producto != null) {
                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (producto != null) {
                Text(
                    text = "Precio: $${producto.precio}",
                    fontSize = 16.sp
                )
            }
        }

        // Ingredientes del producto
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 8.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (producto != null) {
                Text(
                    text = "Ingredientes: ${producto.detalles}",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}