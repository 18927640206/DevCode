package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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

class PagoConfirmado : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

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

                Button(
                    onClick = { navigator.replaceAll(InterfazDeUsuario()) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Aceptar", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(8.dp))
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