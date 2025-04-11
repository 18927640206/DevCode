package org.luisitobez.burgerved.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import cafe.adriel.voyager.navigator.Navigator
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.controller.CarritoController
import org.luisitobez.burgerved.model.domain.Pedido

// Variable global para almacenar el servidor
private var server: HttpServer? = null


fun startServer(navigator: Navigator, pedido: Pedido, carritoController: CarritoController) {
    val port = 8080

    // Detiene el servidor si ya está corriendo antes de iniciarlo de nuevo
    stopServer()

    try {
        server = HttpServer.create(InetSocketAddress(port), 0).apply {
            createContext("/payment_success") { exchange ->
                carritoController.realizarPago(pedido, pedido.total_pago, "PayPal")
                navigator.push(PagoConfirmado(pedido))  // Cambia a la pantalla de éxito
                exchange.sendResponseHeaders(200, "OK".toByteArray().size.toLong())
                exchange.responseBody.use { it.write("OK".toByteArray()) }
            }

            createContext("/payment_cancel") { exchange ->
                navigator.push(PagoCancelado())  // Cambia a la pantalla de cancelación
                exchange.sendResponseHeaders(200, "CANCEL".toByteArray().size.toLong())
                exchange.responseBody.use { it.write("CANCEL".toByteArray()) }
            }

            executor = null
            start()
            println(" Servidor iniciado en http://localhost:$port")
        }
    } catch (e: java.net.BindException) {
        println(" Error: El puerto $port ya está en uso.")
    }
}

// Función para detener el servidor
fun stopServer() {
    server?.stop(0)  // Detiene el servidor si está en ejecución
    server = null
    println("Servidor detenido correctamente.")
}