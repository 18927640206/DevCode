package org.luisitobez.burgerved.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.controller.AppController

@Composable
fun ProductoItem(
    producto: Producto,
    onAddToCart: () -> Unit
) {
    Button(
        onClick = {
            onAddToCart()
            //pedidoDetalleDAO.addProducto( pedido, producto,1)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*Image(
                painter = painterResource("commonMain\\composeResources\\drawable\\rb_2151137700.png"),
                contentDescription = null, // Descripción de la imagen para accesibilidad
                modifier = Modifier
                    .size(64.dp) // Tamaño de la imagen
                    .padding(end = 8.dp) // Espacio entre la imagen y el texto
            )*/
            Text(text = producto.nombre, color = Color.Black)
            Text(text = "$${producto.precio}", color = Color.Black)
        }
    }
}


@Composable
fun ProductoButton(productoId: Int, onProductoSelected: (Int) -> Unit) {
    Button(
        onClick = { onProductoSelected(productoId) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = null
    ) {
        Text(
            text = "Producto $productoId",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}