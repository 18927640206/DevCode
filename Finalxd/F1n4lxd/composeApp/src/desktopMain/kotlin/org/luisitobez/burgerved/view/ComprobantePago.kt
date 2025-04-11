package org.luisitobez.burgerved.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import finalxd.composeapp.generated.resources.Res
import finalxd.composeapp.generated.resources.codigoQR
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.luisitobez.burgerved.controller.ProductoController
import org.luisitobez.burgerved.model.data.ConexionDB
import org.luisitobez.burgerved.model.data.PedidoProductoDAOImpl
import org.luisitobez.burgerved.model.data.ProductoDAOImpl
import org.luisitobez.burgerved.model.data.generarPdfComprobante
import org.luisitobez.burgerved.model.domain.Pedido


class ComprobantePago(private val pedido: Pedido) : Screen {

    // Composable principal para mostrar el contenido del comprobante
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mostrarImagen = remember { mutableStateOf(false) }
        val tiempoRestante = remember { mutableStateOf(30) }
        val productoController = remember {
            val conexion = ConexionDB()
            ProductoController(
                productoDAO = ProductoDAOImpl(conexion),
                pedidoProductoDAO = PedidoProductoDAOImpl(conexion)
            )
        }

        // Obtener productos del pedido
        val productosDelPedido = remember {
            productoController.pedirPedidoProductos(pedido)

        }

        // Agrupar productos por ID para contar la cantidad y calcular precios
        val productosAgrupados = remember {
            productosDelPedido
                .groupBy { it.idProducto }
                .mapNotNull { (idProducto, lista) ->
                    val producto = productoController.obtenerProductoPorId(idProducto.toString())
                    producto?.let {
                        val cantidad = lista.size
                        Triple(it, cantidad, lista.first().precioUnitario)
                    }
                }
        }

        LaunchedEffect(Unit) {
            generarPdfComprobante(pedido)
            mostrarImagen.value = true
            while (tiempoRestante.value > 0) {
                delay(1000)  // 1 segundo
                tiempoRestante.value -= 1  // Reducir el tiempo en 1 segundo
            }
            navigator.replaceAll(InterfazDeUsuario())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF28001)),  // Fondo anaranjado
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF042E46))  // Fondo azul oscuro
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BurgerVend",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Contenedor principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  // Este contenido ocupa el espacio disponible restante
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()), // Habilitar scroll vertical
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Título
                Text(
                    text = "Comprobante de Pago",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Caja de detalles del comprobante
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    ComprobanteItem("ID Pedido:", "#${pedido.id}")
                    ComprobanteItem("Fecha y Hora:", "${pedido.fecha_hora}")
                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = "Productos:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    productosAgrupados.forEach { (producto, cantidad, precioUnitario) ->
                        ComprobanteItem(
                            label = "${cantidad}x ${producto.nombre}",
                            value = "$${"%.2f".format(precioUnitario * cantidad)}"
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    ComprobanteItem("Método de pago:", pedido.metodo_pago)
                    ComprobanteItem("Descuento aplicado:", "$${"%.2f".format(pedido.descuento)}")
                    ComprobanteItem(
                        label = "Total:",
                        value = "$${"%.2f".format(pedido.total_pago)}",
                        isTotal = true
                    )

                    // Mostrar el temporizador en segundos
                    Text(
                        text = "Tiempo restante: ${tiempoRestante.value} segundos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                // Mostrar la imagen del QR si es necesario
                if (mostrarImagen.value) {
                    val imagen = painterResource(resource = Res.drawable.codigoQR)

                    Image(
                        painter = imagen,
                        contentDescription = "Código QR",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navigator.replaceAll(InterfazDeUsuario())
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A0DAD)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)  // Elimina .weight(1f) si no es necesario
                ) {
                    Text("Finalizar", fontSize = 18.sp, color = Color.White)
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

    // Composable para mostrar un elemento de información del comprobante (etiqueta y valor)
    @Composable
    private fun ComprobanteItem(label: String, value: String, isTotal: Boolean = false) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (isTotal) Color(0xFF6A0DAD) else Color.Black
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = if (isTotal) Color(0xFF6A0DAD) else Color.DarkGray
            )
        }
    }
}
