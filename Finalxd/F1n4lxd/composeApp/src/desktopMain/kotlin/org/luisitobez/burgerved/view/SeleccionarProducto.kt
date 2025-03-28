package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.PedidoProductos
import org.luisitobez.burgerved.model.domain.Producto

class SeleccionIngrediente(
    private val producto: Producto,
    private val pedido: Pedido,
    private val pedidoProductos: PedidoProductos
) : Screen {
    @Composable
    override fun Content() {
        val appController = remember { AppController() }
        val ingredienteController = appController.ingredienteController
        val productoController = appController.productoController
        val productoIngredienteController = appController.productoIngredienteController
        val ingredientesExtraController = appController.ingredientesExtraController

        val navigator = LocalNavigator.currentOrThrow

        // Estado para almacenar el precio total adicional
        var total by remember { mutableStateOf(0f) }
        val ingredientes = ingredienteController.obtenerTodosIngrediente()
        val productoIngredientes = productoIngredienteController.obtenerIngredientesDeProducto(producto)

        val cantidades = remember { mutableStateMapOf<Int, Int>() }

        LaunchedEffect(productoIngredientes) {
            productoIngredientes?.forEach { pi ->
                cantidades[pi.idIngrediente] = pi.cantidad
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Panel Norte
            Surface(
                color = Color(0xFF042E46),
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = producto.nombre,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Panel Centro: Lista de ingredientes
            LazyColumn(
                modifier = Modifier.weight(1f).background(Color(0xFFF28001)).fillMaxWidth()
            ) {
                itemsIndexed(ingredientes) { index, ingrediente ->
                    val cantidad = cantidades[ingrediente.idIng] ?: 0

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        // Nombre del ingrediente
                        Text(
                            text = ingrediente.nombre,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        // Botón para decrementar la cantidad
                        Button(
                            onClick = {
                                if (cantidad > 0) {
                                    cantidades[ingrediente.idIng] = cantidad - 1
                                    total -= ingrediente.precio
                                }
                            },
                            enabled = cantidad > 0,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("-")
                        }

                        // Cantidad actual
                        Text(
                            text = "$cantidad",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // Botón para incrementar la cantidad
                        Button(
                            onClick = {
                                cantidades[ingrediente.idIng] = cantidad + 1
                                total += ingrediente.precio
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("+")
                        }
                    }
                }
            }

            // Panel Sur: Precio y botón de confirmar
            Surface(
                color = Color(0xFF042E46),
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = "Precio Hambur: $${producto.precio + total}",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            val preciofinal = producto.precio + total
                            productoController.cambiarprecioDeProducto(pedidoProductos, preciofinal)
                            navigator.pop()
                        }
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}