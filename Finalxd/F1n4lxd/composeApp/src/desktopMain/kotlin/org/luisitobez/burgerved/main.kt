package org.luisitobez.burgerved

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.luisitobez.burgerved.controller.AppController


fun main() = application {

    //prueba para reestablecer stock 
    /*
    //  Crea el controlador
    val appController = AppController()

    // PRUEBA TEMPORAL - Restablecer stock (quitar después de probar)
    val idIngredientePrueba = 1    // Cambia al ID que quieras probar
    val cantidadPrueba = 50        // Cantidad a agregar

    println("=== INICIO PRUEBA ===")
    println("Intentando agregar $cantidadPrueba unidades al ingrediente $idIngredientePrueba")

    appController.ingredienteController.restablecerIngrediente(idIngredientePrueba, cantidadPrueba)

    println("=== FIN PRUEBA ===")
   */

    //val appController = AppController()
    //println(appController.ingredienteController.generarAlertasStockAgotado()) //para pruebas, borrar despues

    Window(
        onCloseRequest = ::exitApplication,
        title = "Finalxd",
    ) {
        App()
    }
}
