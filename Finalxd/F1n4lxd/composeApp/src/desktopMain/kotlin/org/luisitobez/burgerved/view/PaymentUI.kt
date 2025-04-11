package org.luisitobez.burgerved.view

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
import kotlinx.coroutines.delay
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.EstadoProgramado
import java.awt.Desktop
import java.net.URI
import java.time.LocalDateTime

class PaymentUI(
    private var pedido: Pedido,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val appController = remember { AppController() }
        val carritoController = appController.carritoController
        val pedidoController = appController.pedidoController
        var confirmationMessage by remember { mutableStateOf("") }
        var selectedPaymentMethod by remember { mutableStateOf("Efectivo") } // Valor por defecto

        // Iniciar el servidor HTTP para escuchar respuestas

        LaunchedEffect(Unit) {
            startServer(navigator, pedido, carritoController)
            delay(180000)
            navigator.pop()
        }

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

            // Sección de pago
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total a pagar: $${pedido.total_pago}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // Selección del método de pago
                Text("Selecciona un método de pago:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    RadioButton(
                        selected = selectedPaymentMethod == "Efectivo",
                        onClick = { selectedPaymentMethod = "Efectivo" }
                    )
                    Text("Efectivo", fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp))

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = selectedPaymentMethod == "PayPal",
                        onClick = { selectedPaymentMethod = "PayPal" }
                    )
                    Text("PayPal", fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    when (selectedPaymentMethod) {
                        "Efectivo" -> navigator.push(PanelPagoEfectivo(pedido))
                        "PayPal" -> {
                            // URL donde se encuentra tu archivo PHP para manejar el pago
                            val url = "http://localhost/curso/pasarela/index.php?totalAmount=${pedido.total_pago}"
                            // Verifica si el escritorio tiene la capacidad de abrir un navegador
                            if (Desktop.isDesktopSupported()) {
                                val desktop = Desktop.getDesktop()
                                try {
                                    desktop.browse(URI(url))  // Abre la URL en el navegador predeterminado
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        else -> {
                            // Manejo de otros métodos de pago si es necesario
                            println("Método de pago no válido")
                        }
                    }
                }) {
                    Text("Confirmar Pago", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Boton para programar pedido
                Button(
                    onClick = {
                        navigator.push(ProgramarPedido(
                            pedido = pedido,
                            pedidoController = pedidoController,
                            onTimeSelected = { pedidoProgramado ->
                            pedido = pedidoProgramado
                            navigator.push(PagoConfirmado(pedidoProgramado))
                            navigator.pop()
                        }))
                    }
                ) {
                    Text("Programar Pedido", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (confirmationMessage.isNotEmpty()) {
                    Text(text = confirmationMessage, fontSize = 20.sp, color = Color.Black)
                }
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
                    onClick = { navigator.pop() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))
                ) {
                    Text("Regresar", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}