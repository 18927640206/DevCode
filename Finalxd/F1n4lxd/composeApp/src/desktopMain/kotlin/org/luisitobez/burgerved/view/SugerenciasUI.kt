package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.luisitobez.burgerved.controller.ProductoController
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.PedidoProductos
import org.luisitobez.burgerved.model.domain.Producto

class SugerenciasUI(
    private val pedido: Pedido,
    private val productosPedido: List<PedidoProductos>
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appController = remember { AppController() }
        val productoController = appController.productoController

        var productosSugeridos by remember { mutableStateOf<List<Producto>>(emptyList()) }
        var total by remember { mutableStateOf(productosPedido.sumOf { it.precioUnitario.toDouble() }.toFloat()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var currentPedidoProductos by remember { mutableStateOf(productosPedido) }

        LaunchedEffect(Unit) {
            try {
                productosSugeridos = productoController.obtenerSugerenciasParaPedido(pedido)
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar sugerencias: ${e.message}"
                isLoading = false
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF28001)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF042E46)).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Sugerencias para tu pedido", fontSize = 28.sp, color = Color.White)
            }

            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp)
            ) {
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    errorMessage != null -> Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Sección de productos existentes
                        if (currentPedidoProductos.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Tu pedido actual",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            items(currentPedidoProductos) { pedidoProducto ->
                                ProductoExistenteItem(
                                    pedidoProducto = pedidoProducto,
                                    productoController = productoController
                                )
                            }
                        }

                        // Sección de sugerencias
                        if (productosSugeridos.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Sugerencias",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            items(productosSugeridos) { producto ->
                                SugerenciaItem(
                                    producto = producto,
                                    onAddToCart = {
                                        productoController.agregarProductoAPedido(pedido, producto)
                                        total += producto.precio.toFloat()
                                        productosSugeridos = productosSugeridos.filter { it.id != producto.id }
                                        currentPedidoProductos = productoController.pedirPedidoProductos(pedido)
                                    }
                                )
                            }
                        } else if (!isLoading && errorMessage == null) {
                            item {
                                Text(
                                    text = "No hay sugerencias disponibles",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navigator.pop() }) {
                    Text("Regresar", fontSize = 24.sp)
                }

                Button(onClick = {
                    pedido.total_pago = total // Actualiza el total con las sugerencias añadidas
                    navigator.push(PaymentUI(pedido)) // Luego a PaymentUI
                }) {
                    Text("Omitir/Pagar", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
private fun ProductoExistenteItem(
    pedidoProducto: PedidoProductos,
    productoController: ProductoController
) {
    val producto = remember { productoController.obtenerProductoPorId(pedidoProducto.idProducto.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto?.nombre ?: "Producto no encontrado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Precio unitario", // Cambiado a texto fijo
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "$${"%.2f".format(pedidoProducto.precioUnitario)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF042E46)
                )
            }
        }
    }
}

@Composable
private fun SugerenciaItem(
    producto: Producto,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = producto.detalles,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${"%.2f".format(producto.precio)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF042E46)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddToCart,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF28001))
                    ) {
                        Text("Añadir", color = Color.White)
                    }
                }
            }
        }
    }
}