package org.luisitobez.burgerved.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.luisitobez.burgerved.model.domain.Producto


@Composable
fun CarritoItem(producto: Producto, onRemove: () -> Unit, onIngredientesClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = producto.nombre, fontSize = 18.sp)
                Text(text = "$${producto.precio}", fontSize = 18.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onRemove) { Text("Eliminar") }
                Button(onClick = onIngredientesClick) { Text("Ingredientes") }
            }
        }
    }
}