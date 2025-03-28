package org.luisitobez.burgerved.view;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

class PanelPagoEfectivo(
    private val pedido: Pedido
) : Screen {
    private val appController = AppController()
    private val productoController = appController.productoController
    private val carritoController = appController.carritoController
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var insertedAmount by remember { mutableStateOf(0f) }
        val appController = remember { AppController() }
        val carritoController = appController.carritoController

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BurguerVend",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Sección de pago en efectivo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Total a pagar: $${pedido.total_pago}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Ingrese la cantidad necesaria para comprar su pedido.",
                    fontSize = 25.sp,
                    color = Color.Black
                )
                Text("Ingresado: $$insertedAmount", fontSize = 25.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = { insertedAmount += 1f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("+ $1", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { insertedAmount += 5f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("+ $5", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { insertedAmount += 10f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("+ $10", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { insertedAmount += 100f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("+ $100", fontSize = 18.sp, color = Color.White)
                    }
                }

                // Nuevos botones para restar monedas
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = { if (insertedAmount >= 1f) insertedAmount -= 1f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("- $1", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (insertedAmount >= 5f) insertedAmount -= 5f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("- $5", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (insertedAmount >= 10f) insertedAmount -= 10f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("- $10", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (insertedAmount >= 100f) insertedAmount -= 100f },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                    ) {
                        Text("- $100", fontSize = 18.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (insertedAmount >= pedido.total_pago) {
                            carritoController.realizarPago(pedido, pedido.total_pago, "Efectivo")
                            val productosEnCarrito = appController.pedidoController.obtenerProductosDelPedido(pedido)

                            productosEnCarrito.forEach { item ->
                                productoController.procesarVenta(
                                    item.idProducto,  
                                    item.cantidad
                                )
                            }
                            navigator.push(PagoConfirmado())
                        }
                    },
                    enabled = insertedAmount >= pedido.total_pago,
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (insertedAmount >= pedido.total_pago) Color.Green else Color.Gray)
                ) {
                    Text("Confirmar Pago", fontSize = 24.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botones inferiores
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navigator.pop()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                ) {
                    Text("Cancelar", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}
