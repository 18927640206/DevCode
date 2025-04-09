package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.AppController

class ModificarIngredientes: Screen {
    @Composable
    override fun Content() {
        val appController = remember { AppController() }
        val ingredienteController = appController.ingredienteController
        val ingredientes = ingredienteController.obtenerTodosIngrediente()
        val navigator = LocalNavigator.currentOrThrow

        // Usamos un mapa para almacenar los cambios temporales (String para manejar el texto)
        val cantidadesTemporales = remember { mutableStateMapOf<Int, String>() }

        // Inicializamos el mapa con valores vacíos
        ingredientes.forEachIndexed { index, _ ->
            if (!cantidadesTemporales.containsKey(index)) {
                cantidadesTemporales[index] = ""
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado
            Surface(
                color = Color(0xFF042E46),
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Modificar Stock",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Lista de ingredientes
            LazyColumn(
                modifier = Modifier.weight(1f).background(Color(0xFFF28001)).fillMaxWidth()
            ) {
                itemsIndexed(ingredientes) { index, ingrediente ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(8.dp)
                    ) {
                        // Nombre del ingrediente
                        Text(
                            text = ingrediente.nombre,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        // Stock actual
                        Text(
                            text = "Stock: ${ingrediente.stock}",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // Campo para nueva cantidad
                        OutlinedTextField(
                            value = cantidadesTemporales[index] ?: "",
                            onValueChange = { newValue ->
                                // Validamos que solo contenga números
                                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*$"))) {
                                    cantidadesTemporales[index] = newValue
                                }
                            },
                            label = { Text("Nuevo") },
                            modifier = Modifier.width(80.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.Black,
                                backgroundColor = Color.White,
                                focusedBorderColor = Color(0xFF042E46),
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp)
                        )

                        // Botón para actualizar
                        Button(
                            onClick = {
                                val nuevaCantidadStr = cantidadesTemporales[index] ?: ""
                                if (nuevaCantidadStr.isNotBlank()) {
                                    val nuevaCantidad = nuevaCantidadStr.toIntOrNull()
                                    if (nuevaCantidad != null) {
                                        ingredienteController.actualizarStock(
                                            ingrediente.idIng,
                                            nuevaCantidad
                                        )
                                        // Limpiar el campo después de actualizar
                                        cantidadesTemporales[index] = ""
                                    }
                                }
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Actualizar")
                        }
                    }
                }
            }

            // Pie de página
            Surface(
                color = Color(0xFF042E46),
                modifier = Modifier.fillMaxWidth().height(100.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF042E46))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            // Actualizar todos los ingredientes con valores modificados
                            ingredientes.forEachIndexed { index, ingrediente ->
                                val nuevaCantidadStr = cantidadesTemporales[index] ?: ""
                                if (nuevaCantidadStr.isNotBlank()) {
                                    val nuevaCantidad = nuevaCantidadStr.toIntOrNull()
                                    if (nuevaCantidad != null) {
                                        ingredienteController.actualizarStock(
                                            ingrediente.idIng,
                                            nuevaCantidad
                                        )
                                    }
                                }
                            }
                            navigator.pop()
                        },

                    ) {
                        Text("Guardar Todo", fontSize = 24.sp)
                    }

                    Button(
                        onClick = { navigator.pop() },

                    ) {
                        Text("Cancelar", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}