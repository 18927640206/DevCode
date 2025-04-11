package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.border // Import añadido para resolver el error 'border'
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.controller.CarritoController
import org.luisitobez.burgerved.model.domain.Pedido

class ProcesandoPedidoScreen(
    private var pedido: Pedido,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var progreso by remember { mutableStateOf(0f) }
        val appController = remember { AppController() }
        val carritoController = appController.carritoController

        LaunchedEffect(Unit) {
            while (progreso < 1f) {
                delay(100L)
                progreso += 0.01f
                if (progreso >= 1f) {
                    delay(1000L)
                    navigator.push(PedidoListoScreen(pedido, carritoController))
                }
            }
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

            // Contenido central
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF042E46)), // Error corregido con el import
                    contentAlignment = Alignment.Center
                ) {
                    Text("🍔", fontSize = 80.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Preparando tu pedido...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                LinearProgressIndicator(
                    progress = progreso,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(horizontal = 32.dp),
                    color = Color(0xFF042E46),
                    backgroundColor = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progreso * 100).toInt()}% completado",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            // Pie de página
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Gracias por su paciencia",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

class PedidoListoScreen(
    private var pedido: Pedido,
    private var carritoController: CarritoController
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

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

            // Contenido central
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF042E46)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 80.sp, color = Color.Green)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Pedido Listo!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Por favor recoge tu pedido",
                    fontSize = 20.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón "Aceptar"
                    Button(
                        onClick = { navigator.replaceAll(InterfazDeUsuario()) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .widthIn(min = 120.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text("Volver al inicio", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                    }

                    // Botón "Ver Comprobante"
                    Button(
                        onClick = {
                            pedido = carritoController.obtenerUltimoPedido()
                            navigator.push(ComprobantePago(pedido))
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .widthIn(min = 120.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text("Ver Comprobante", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                    }
                }


            }

            // Pie de página
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Gracias por elegir BurguerVend",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}