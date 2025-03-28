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
    private val pedido: Pedido,
    private val montoAhorrado: Float
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow


        var confirmationMessage by remember { mutableStateOf("") }
        var selectedPaymentMethod by remember { mutableStateOf("Efectivo") } // Valor por defecto

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
                Text("¡Estas ahorrando $${montoAhorrado}!", fontSize = 24.sp, color = Color.Green)
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
                        "PayPal" -> confirmationMessage = "Redirigiendo a PayPal para el pago..."
                        else -> confirmationMessage = "Método de pago no válido"
                    }
                }) {
                    Text("Confirmar Pago", fontSize = 24.sp)
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