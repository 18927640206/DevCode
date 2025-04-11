package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.controller.UsuarioController

/**
 * Pantalla de inicio de sesión para administradores.
 *
 * Esta pantalla permite a los usuarios autenticarse para acceder al modo administrador.
 * Presenta un formulario con campos para usuario y contraseña, validación de credenciales
 * y navegación a otras pantallas según el estado de autenticación.
 *
 * La interfaz se divide en tres secciones principales:
 * 1. Encabezado con el título de la aplicación
 * 2. Formulario de inicio de sesión o menú de administrador (según estado de autenticación)
 * 3. Pie de página con botón para salir de la aplicación
 */
class InicioSesion : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appController = remember { AppController() }
        val usuarioController = appController.usuarioController

        // Estados para los campos del formulario
        val usuario = remember { mutableStateOf("") }
        val contrasena = remember { mutableStateOf("") }
        val errorMensaje = remember { mutableStateOf<String?>(null) }
        var estado by rememberSaveable { mutableStateOf(usuarioController.administrarSesion("", "s")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)), // Color naranja de fondo
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Sección 1: Encabezado con título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46)) // Color azul oscuro
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BurguerVend: Modo admin",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Sección 2: Contenido principal (formulario o menú según autenticación)
            if (!estado) {
                // Formulario de inicio de sesión
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mensaje de error si existe
                    errorMensaje.value?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Campo de usuario
                    OutlinedTextField(
                        value = usuario.value,
                        onValueChange = { usuario.value = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                            focusedBorderColor = Color(0xFF042E46),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = contrasena.value,
                        onValueChange = { contrasena.value = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                            focusedBorderColor = Color(0xFF042E46),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de inicio de sesión
                    Button(
                        onClick = {
                            // Validación del formulario
                            when {
                                usuario.value.isEmpty() ->
                                    errorMensaje.value = "Ingrese un usuario"
                                contrasena.value.isEmpty() ->
                                    errorMensaje.value = "Ingrese una contraseña"
                                !usuarioController.administrarSesion(usuario.value, contrasena.value) -> {
                                    errorMensaje.value = "Usuario o contraseña incorrectos"
                                }
                                else -> {
                                    // Autenticación exitosa
                                    errorMensaje.value = null
                                    estado = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        content = {
                            Text("INICIAR SESIÓN", fontSize = 20.sp)
                        }
                    )
                }
            } else {
                // Menú de administrador (post-autenticación)
                Column {
                    Button(
                        onClick = { navigator.push(ModificarIngredientes()) },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Modificar Ingredientes")
                    }
                }
            }

            // Sección 3: Pie de página con botón de salida
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navigator.pop() }
                ) {
                    Text("SALIR", fontSize = 24.sp)
                }
            }
        }
    }
}