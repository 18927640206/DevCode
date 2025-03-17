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
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.Producto
import org.luisitobez.burgerved.controller.AppController


class PaymentUI(
    private val totalAmount: Float, // Se cambia el nombre para mayor claridad
    private val pedido: Pedido,
    private val carrito: List<Producto>
) : Screen {
    @Composable
    override fun Content() {
        val appController = remember { AppController() }
        val carritoController = appController.carritoController

        val navigator = LocalNavigator.currentOrThrow
        var confirmationMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF28001)),
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
                Text("Total a pagar: $$totalAmount", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    confirmationMessage = /*"Pago exitoso por $$totalAmount"*/ carritoController.realizarPago(pedido,totalAmount) //pago realizacion en controller y no vista
                }) {
                    Text("Confirmar Pago", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = confirmationMessage, fontSize = 20.sp, color = Color.Black)
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
                Button(onClick = { navigator.pop() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))) {
                    Text("Cancelar", fontSize = 24.sp, color = Color.White)
                }
                Button(onClick = { navigator.push(InterfazDeUsuario(pedido, 1, carrito)) }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD))) {
                    Text("Pagar", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}
