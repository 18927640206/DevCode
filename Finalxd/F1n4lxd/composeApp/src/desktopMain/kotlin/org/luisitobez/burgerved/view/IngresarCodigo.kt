package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.Pedido

class IngresarCodigo(
    private val pedido: Pedido
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val pedidoController = remember { AppController().pedidoController }
        var codigoIngresado by remember { mutableStateOf("") }
        var mensajeError by remember { mutableStateOf<String?>(null) }
        var mostrarExito by remember { mutableStateOf(false) }
        var pedidoEntregado by remember { mutableStateOf<Pedido?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
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

            Column(
                modifier = Modifier.weight(1f).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (mostrarExito) {
                    Text(
                        "¡Disfruta tu comida!",
                        color = Color.Green,
                        fontSize = 20.sp
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Resumen del Pedido",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            pedidoEntregado?.let { pedido ->
                                Text("Total: $${pedido.total_pago}")
                                Text("Método de pago: ${pedido.metodo_pago}")
                                if (pedido.descuento > 0) {
                                    Text("Descuento: ${pedido.descuento * 100}%")
                                    Text("Ahorrado: $${pedido.montoAhorrado}")
                                }
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = codigoIngresado,
                        onValueChange = { codigoIngresado = it },
                        label = { Text("Ingrese código de 6 dígitos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensajeError != null) {
                        Text(
                            mensajeError!!,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            try {
                                val pedido = pedidoController.verificarCodigo(codigoIngresado)
                                if (pedido != null) {
                                    pedidoController.marcarPedidoEntregado(pedido)
                                    pedidoEntregado = pedido
                                    mostrarExito = true
                                    mensajeError = null
                                } else {
                                    mensajeError = "Código incorrecto o pedido no disponible"
                                }
                            } catch (e: Exception) {
                                mensajeError = "Error al procesar el pedido: ${e.message}"
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Confirmar codigo")
                    }
                }
            }
            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navigator.pop() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF6A0DAD),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("Regresar", fontSize = 18.sp)
                }
            }
        }
    }
}