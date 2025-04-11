package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.PedidoProductos


class InterfazDeUsuario() : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val appController = remember { AppController() }
        val productoController = appController.productoController
        val carritoController = appController.carritoController
        var pedido by rememberSaveable { mutableStateOf(carritoController.agregarPedido()) }
        val navigator = LocalNavigator.currentOrThrow
        var contador by rememberSaveable { mutableStateOf(0) }
        var productospedido by remember { mutableStateOf<List<PedidoProductos>>(emptyList()) }
        var precioTotal by remember { mutableStateOf(0.0f) }
        val focusRequester = remember { FocusRequester() }

        var notificarDescuento by remember { mutableStateOf("") }
        var pedidoConDescuento by rememberSaveable { mutableStateOf(pedido) }
        var montoAhorrado by rememberSaveable { mutableStateOf(0f) }
        var totalConDescuento by rememberSaveable { mutableStateOf(0f) }
        var botonAdmin by remember { mutableStateOf(false) }

        // Obtener productos y bebidas
        val productos = productoController.obtenerTodosProductos()
        val bebidas = productoController.obtenerTodasBebidas()

        productospedido = productoController.pedirPedidoProductos(pedido)

        // Actualizar el precio total cuando cambie la lista de productos
        LaunchedEffect(productospedido) {
            precioTotal = productospedido.sumOf { it.precioUnitario.toDouble() }.toFloat()
            pedidoConDescuento =
                carritoController.aplicarDescuento(pedido.copy(total_pago = precioTotal), contador)
            montoAhorrado = pedidoConDescuento.montoAhorrado
            totalConDescuento = pedidoConDescuento.total_pago
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001))
                .focusable()
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.isCtrlPressed) {
                        botonAdmin = true// Alternar estado con Ctrl+A
                        println("Hola")
                        true
                    } else {
                        botonAdmin = false// Alternar estado con Ctrl+A
                        false
                    }
                },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título
            if(botonAdmin){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF042E46))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BurguerVend: Modo admin",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Button(onClick = {navigator.push(InicioSesion())}) {
                    Text("Iniciar Secion", fontSize = 24.sp)
                }
            }else{
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

                                            productoController.agregarProductoAPedido(
                                                pedido,
                                                producto
                                            )
                                            productospedido =
                                                productoController.pedirPedidoProductos(pedido)
                                            notificarDescuento =
                                                carritoController.notificarDescuento(contador)
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
                                            productoController.agregarProductoAPedido(
                                                pedido,
                                                producto
                                            )
                                            productospedido =
                                                productoController.pedirPedidoProductos(pedido)
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
                                        productospedido =
                                            productoController.pedirPedidoProductos(pedido)
                                    } catch (e: Exception) {
                                        println("Error al eliminar el producto del pedido: ${e.message}")
                                    }
                                },
                                onIngredientesClick = {
                                    productoController.obtenerProductoPorId("${producto.idProducto}")
                                        ?.let {
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

            //Notificacion de descuento
            if (notificarDescuento.isNotEmpty()) {
                Text(
                    text = notificarDescuento,
                    color = Color.Green,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(Color(0xFF042E46).copy(alpha = 0.7f))
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botones en fila horizontal
                    Row(
                        modifier = Modifier.width(380.dp),  // Ancho aumentado para mejor distribución
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón Reportar
                        Button(
                            onClick = { navigator.push(ReporteProblemas(pedido)) },
                            modifier = Modifier
                                .weight(1f)
                                .height(49.dp)  // Altura aumentada
                        ) {
                            Text("Reportar",
                                fontSize = 18.sp,  // Tamaño de fuente aumentado
                                fontWeight = FontWeight.Medium)
                        }

                        // Botón Cancelar
                        Button(
                            onClick = {
                                precioTotal = 0.0f
                                carritoController.eliminarPedido(pedido)
                                pedido = appController.pedidoController.agregarPedido()!!
                                productospedido = productoController.pedirPedidoProductos(pedido)
                            },enabled = productospedido.isNotEmpty(),
                            modifier = Modifier
                                .weight(1f)
                                .height(49.dp)  // Altura uniforme
                        ) {
                            Text("Cancelar",
                                fontSize = 18.sp,  // Tamaño de fuente aumentado
                                fontWeight = FontWeight.Medium)
                        }

                        // Botón para ingresar código
                        Button(
                            onClick = { navigator.push(IngresarCodigo(pedido)) },
                            modifier = Modifier
                                .weight(1.2f)  // Un poco más ancho para el texto más largo
                                .height(49.dp)
                        ) {
                            Text("Ingresar Código",  // Texto completo
                                fontSize = 16.sp,  // Tamaño ligeramente menor para el texto más largo
                                fontWeight = FontWeight.Medium)
                        }
                    }

                    // Precios (centro)
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Precio: $${precioTotal}",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Descuento: $${montoAhorrado}",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Total a pagar: $${totalConDescuento}",
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botón Pagar
                    Button(
                        onClick = {
                            pedido.total_pago = totalConDescuento
                            carritoController.actualizarPrecioPedido(pedido, totalConDescuento)
                            navigator.push(SugerenciasUI(pedido, productospedido, montoAhorrado))
                        }, enabled = productospedido.isNotEmpty()
                    ) {
                        Text("Pagar", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}