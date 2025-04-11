package org.luisitobez.burgerved.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.luisitobez.burgerved.controller.CarritoController
import org.luisitobez.burgerved.model.domain.Pedido
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import org.luisitobez.burgerved.controller.AppController



class PagoConfirmado(
    private var pedido: Pedido,
)  : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val appController = remember { AppController() }
        val carritoController = appController.carritoController

        // Navegar automáticamente después de 3 segundos


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
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "Pago Confirmado",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Gracias por su compra!",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 36.dp)
                )
                if (pedido.pedidoProgramado) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Pedido programado",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Codigo para recoger pedido: ${pedido.codigoRecoger}",
                        fontSize = 22.sp,
                        color = Color.White
                    )

                    Text(
                        text = "Hora maxima para recoger pedido: ${pedido.horaRecoger?.formatHora() ?: "" }",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(36.dp)) // Espacio inferior


                // Botón de aceptar (opcional, puedes quitarlo si no lo necesitas)
                Button(
                    onClick = { navigator.push(ProcesandoPedidoScreen(pedido)) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD)),
                    shape = RoundedCornerShape(12.dp)

                ){
                    Text("Aceptar", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
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
@Composable
private fun LocalDateTime.formatHora(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}
