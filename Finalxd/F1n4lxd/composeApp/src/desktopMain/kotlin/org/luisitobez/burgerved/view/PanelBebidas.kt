package org.luisitobez.burgerved.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import finalxd.composeapp.generated.resources.Res
import finalxd.composeapp.generated.resources.Soda
import org.jetbrains.compose.resources.painterResource
import org.luisitobez.burgerved.model.domain.Producto

@Composable
fun PanelBebidas(
    producto: Producto,
    onAddToCart: () -> Unit
) {
    Button(
        onClick = {
            onAddToCart()
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
            Image(
                painter = painterResource(resource = Res.drawable.Soda),
                contentDescription = null, // Descripción de la imagen para accesibilidad
                modifier = Modifier
                    .size(64.dp) // Tamaño de la imagen
                    .padding(end = 8.dp) // Espacio entre la imagen y el texto
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = producto.nombre, color = Color.Black)
            }


            Text(text = "$${producto.precio}", color = Color.Black)
        }
    }
}