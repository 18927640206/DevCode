package org.luisitobez.burgerved.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.luisitobez.burgerved.model.data.ProblemaPedido

import org.luisitobez.burgerved.model.domain.Reporte

class ConfirmacionReporte(

    private val reporte: Reporte
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val solucion = when(reporte.problema) {
            ProblemaPedido.NO_ENTREGADO -> "Estamos verificando tu pedido. Si no podemos resolverlo en 15 minutos, recibirás un reembolso automático."
            ProblemaPedido.INCOMPLETO -> "Prepararemos los items faltantes y los enviaremos inmediatamente."
            ProblemaPedido.INCORRECTO -> "Estamos enviando un reemplazo inmediatamente."
            ProblemaPedido.COBRO -> "Nuestro equipo de finanzas revisará la transacción en un máximo de 24 horas."
            ProblemaPedido.OTRO -> "Nuestro equipo te contactará dentro de las próximas 2 horas."
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)) //
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Confirmado",
                tint = Color.Green,
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Reporte Enviado",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF042E46),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = "Hemos recibido tu reporte sobre:",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = reporte.problema.descripcion,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF28001),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (reporte.descripcion.isNotEmpty()) {
                Text(
                    text = "Descripción:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text =reporte.descripcion,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = "Solución:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = solucion,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { navigator.popUntilRoot() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF042E46)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Volver al Menú Principal", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
