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
/*
import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.domain.PedidoProductos*/

import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.Pedido


class InterfazDeUsuario(val ped: Pedido, val estado:Int, val carrito: List<Producto>) : Screen {
    @Composable
    override fun Content() {

        val appController = remember { AppController() }
        val productoController = appController.productoController
        val carritoController = appController.carritoController
        /*
        val productoDAO = remember { ProductoDAOImpl() }
        val pedidoDetalleDAO = remember { PedidoProductoDAOImpl() }
        val pedidoDAO = remember { PedidoDAOImpl() }*/

        var pedido by remember { mutableStateOf(ped) }
        var carrito by remember { mutableStateOf(carrito) }
       /* val ingredientesDAO by remember { mutableStateOf(IngredienteDAOImpl()) }
        val pedidoProducto = remember { mutableStateOf(PedidoProductos(0, 0, 0, 0.0f)) }*/
        val navigator = LocalNavigator.currentOrThrow


        val numProductos = /*productoDAOproductoController.getNumeroDeProductos()?.numeroDeProductos ?: 0*/ productoController.obtenerTodosProductos().size
        var precioTotal = carrito.sumOf { it.precio.toDouble() }.toFloat()


        LaunchedEffect(Unit) {
            carritoController.agregarPedido(pedido)
            //pedidoDAO.addPedido(pedido)
            //pedido = pedidoDAO.getPedido()!!
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Panel de productos
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    val productos = productoController.obtenerTodosProductos()
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onAddToCart = {

                              productoController.agregarProductoAPedido(pedido, producto)
                                //carrito = carrito + producto
                               // precioTotal += producto.precio
                            }
                        )

                    }
                }

                // Panel del carrito
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    items(carrito) { producto ->
                        CarritoItem(
                            producto = producto,
                            onRemove = { carrito = carrito - producto },
                            onIngredientesClick = { println("Mostrar ingredientes de ${producto.nombre}") }
                        )
                    }
                }
            }

            // Panel de pago
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    carrito = emptyList()
                    //pedidoDAO.borrarPedido(pedido)
                   // pedidoDAO.addPedido(pedido)
                    //pedido = pedidoDAO.getPedido()!!
                    precioTotal = 0.0f
                    carritoController.eliminarPedido(pedido)
                    carritoController.agregarPedido(pedido)
                }) {
                    Text("Cancelar", fontSize = 24.sp)
                }
                Text(
                    text = "Total a pagar $${precioTotal}",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Button(onClick = { navigator.push(PaymentUI(precioTotal, pedido, carrito)) }) {
                    Text("Pagar", fontSize = 24.sp)
                }
            }
        }
    }
}
