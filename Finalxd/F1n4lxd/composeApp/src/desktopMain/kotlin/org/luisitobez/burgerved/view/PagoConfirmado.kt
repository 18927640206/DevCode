package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

class PagoConfirmado(
    private var pedido: Pedido,
)  : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appController = remember { AppController() }
        val carritoController = appController.carritoController

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)), // Fondo anaranjado
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
                Spacer(modifier = Modifier.height(36.dp)) // Espacio superior

                Text(
                    text = "Pago Confirmado",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el título y el mensaje

                Text(
                    text = "Gracias por su compra!",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                Spacer(modifier = Modifier.height(36.dp)) // Espacio inferior

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
                        Text("Aceptar", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
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
                    text = "Gracias por confiar en BurgerVend",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}