package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.luisitobez.burgerved.model.domain.PedidoProductos

class InterfazDeUsuario() : Screen {
    @Composable
    override fun Content() {
        val appController = remember { AppController() }
        val productoController = appController.productoController
        val carritoController = appController.carritoController
        var pedido by rememberSaveable { mutableStateOf(carritoController.agregarPedido()) }
        val navigator = LocalNavigator.currentOrThrow
        var contador by remember { mutableStateOf(0) }
        var productospedido by remember { mutableStateOf<List<PedidoProductos>>(emptyList()) }
        var precioTotal by remember { mutableStateOf(0.0f) }

        // Obtener productos y bebidas
        val productos = productoController.obtenerTodosProductos()
        val bebidas = productoController.obtenerTodasBebidas()

        productospedido = productoController.pedirPedidoProductos(pedido)

        // Actualizar el precio total cuando cambie la lista de productos
        LaunchedEffect(productospedido) {
            precioTotal = productospedido.sumOf { it.precioUnitario.toDouble() }.toFloat()
        }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF28001)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BurguerVend",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Panel central
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(5.dp)
            ) {
                // Fila superior: Paneles de productos y carrito
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Ocupa el espacio restante
                        .border(width = 2.dp, color = Color.Black)

                ) {
                    // Panel de productos
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        // Lista de productos
                        LazyColumn(
                            modifier = Modifier.weight(2f)
                                .border(width = 1.dp, color = Color.Black)
                                .padding(5.dp)
                        ) {
                            items(productos) { producto ->
                                if (producto.categoria != "Bebida") {
                                    ProductoItem(
                                        producto = producto,
                                        onAddToCart = {
                                            contador++
                                            productoController.agregarProductoAPedido(pedido, producto, contador)
                                            productospedido = productoController.pedirPedidoProductos(pedido)
                                        }
                                    )
                                }
                            }
                        }

                        // Nuevo panel inferior (debajo del panel de productos)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Un tercio de la altura
                                .padding(top = 16.dp) // Espacio entre los paneles
                                .background(Color(0xFFF28001)),
                            //color = Color(0xFF042E46)) // Color de fondo
                        ) {
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                                    .background(Color(0xFFF28001))
                                    .border(width = 1.dp, color = Color.Black)
                                    .padding(5.dp)
                            ) {
                                items(bebidas) { producto ->
                                    PanelBebidas(
                                        producto = producto,
                                        onAddToCart = {
                                            contador++
                                            productoController.agregarProductoAPedido(pedido, producto, contador)
                                            productospedido = productoController.pedirPedidoProductos(pedido)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Panel del carrito
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .border(width = 2.dp, color = Color.Black)
                            .padding(5.dp)
                            .fillMaxHeight()
                    ) {
                        items(productospedido) { producto ->
                            CarritoItem(
                                productopedido = producto,
                                onRemove = {
                                    try {
                                        productoController.eliminarProductoAPedido(producto)
                                        productospedido = productoController.pedirPedidoProductos(pedido)
                                    } catch (e: Exception) {
                                        println("Error al eliminar el producto del pedido: ${e.message}")
                                    }
                                },
                                onIngredientesClick = {
                                    productoController.obtenerProductoPorId("${producto.idProducto}")?.let {
                                        navigator.push(
                                            SeleccionIngrediente(
                                                it,
                                                pedido,
                                                producto
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Panel de pago
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    precioTotal = 0.0f
                    carritoController.eliminarPedido(pedido)
                    pedido = appController.pedidoController.agregarPedido()!!
                    productospedido = productoController.pedirPedidoProductos(pedido)
                }) {
                    Text("Cancelar", fontSize = 24.sp)
                }
                Text(
                    text = "Total a pagar $${precioTotal}",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Button(onClick = {
                    pedido.total_pago = precioTotal
                    carritoController.actualizarPrecioPedido(pedido, precioTotal)
                    navigator.push(PaymentUI(pedido))
                }) {
                    Text("Pagar", fontSize = 24.sp)
                }
            }
        }
    }
}