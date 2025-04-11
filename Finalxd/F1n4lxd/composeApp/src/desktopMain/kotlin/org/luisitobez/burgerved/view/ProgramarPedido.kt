package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.PedidoController
import org.luisitobez.burgerved.model.domain.Pedido
import java.time.LocalDateTime

class ProgramarPedido(
    private val pedido: Pedido,
    private val pedidoController: PedidoController,
    private val onTimeSelected: (Pedido) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)), // Orange background
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46)) // Dark blue header
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selecciona una hora de recogida",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón para 1 hora
                Button(
                    onClick = {
                        val horaRecoger = LocalDateTime.now().plusHours(1)
                        val pedidoProgramado = pedidoController.programarPedido(pedido, horaRecoger)
                        onTimeSelected(pedidoProgramado)
                        navigator.pop()
                    },
                    modifier = Modifier
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF6A0DAD),
                        contentColor = Color.White
                    )
                ) {
                    Text("Recoger en 1 hora", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para 2 horas
                Button(
                    onClick = {
                        val horaRecoger = LocalDateTime.now().plusHours(2)
                        val pedidoProgramado = pedidoController.programarPedido(pedido, horaRecoger)
                        onTimeSelected(pedidoProgramado)
                        navigator.pop()
                    },
                    modifier = Modifier
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF6A0DAD),
                        contentColor = Color.White
                    )
                ) {
                    Text("Recoger en 2 horas", fontSize = 20.sp)
                }
            }

            // Solo botón de regresar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46)) // Dark blue footer
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navigator.pop() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF6A0DAD), // Purple
                        contentColor = Color.White
                    ),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("Regresar", fontSize = 20.sp)
                }
            }
        }
    }
}
