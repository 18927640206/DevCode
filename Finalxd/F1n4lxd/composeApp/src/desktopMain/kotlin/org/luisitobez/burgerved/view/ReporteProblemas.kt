package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import org.luisitobez.burgerved.model.data.EmailService
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.model.domain.ProblemaPedido
import org.luisitobez.burgerved.model.domain.Reporte


class ReporteProblemas(private val pedido: Pedido) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedProblem by remember { mutableStateOf<ProblemaPedido?>(null) }
        var customDescription by remember { mutableStateOf("") }
        var contacto by remember { mutableStateOf("")}
        var showError by remember { mutableStateOf(false)}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "Reportar Problema",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Contenido principal (con espacio para la barra inferior)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Pedido #${pedido.id}",
                        fontSize = 18.sp,
                        color = Color(0xFF042E46),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                //lista de problemas
                items(ProblemaPedido.values()) { problema ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProblem = problema },
                        elevation = 4.dp,
                        backgroundColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedProblem == problema,
                                onClick = { selectedProblem = problema },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFF28001)
                                )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = problema.descripcion,
                                fontSize = 16.sp,
                                color = Color(0xFF042E46)
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = Color.White
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Describa el problema en detalle", // Texto más descriptivo
                                color = Color(0xFF042E46),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = customDescription,
                                onValueChange = { customDescription = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Ej: Mi hamburguesa llegó fría") }, // Texto de ayuda
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFFF28001),
                                    cursorColor = Color(0xFFF28001)
                                )
                            )
                        }
                    }
                }
                // Campo de contacto
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = Color.White
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Datos de contacto",
                                color = Color(0xFF042E46),
                                fontSize = 16.sp
                            )
                            OutlinedTextField(
                                value = contacto,
                                onValueChange = {
                                    contacto = it
                                    showError = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                label = { Text("Teléfono o correo electrónico") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFFF28001),
                                    errorBorderColor = Color.Red
                                ),
                                isError = showError
                            )
                            if (showError) {
                                Text(
                                    text = "Ingrese un contacto válido",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Barra inferior con botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón Regresar
                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier

                        .padding(end = 4.dp)
                        .height(48.dp),
                ) {
                    Text("Regresar", fontSize = 24.sp, color = Color.White)
                }

                // Botón Continuar
                Button(
                    onClick = {
                        if (contacto.isBlank()) {
                            showError = true
                        } else {
                            val reporte = Reporte(
                                idPedido = pedido.id.toLong(),
                                problema = selectedProblem!!,
                                descripcion = if (selectedProblem == ProblemaPedido.OTRO) customDescription else "",
                                contactoCliente = contacto
                            )

                            navigator.push(ConfirmacionReporte(reporte))
                            Thread {
                                try {
                                    EmailService.createDefault().enviarReporte(
                                        destinatario = "soporte@burgerved.com",
                                        reporte = reporte
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    // Opcional: Mostrar error al usuario
                                }
                            }.start()


                        }
                    },
                    enabled = selectedProblem != null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(48.dp),
                ) {
                    Text("Enviar", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}