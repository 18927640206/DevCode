package org.luisitobez.burgerved

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.view.InterfazDeUsuario

@OptIn(ExperimentalStdlibApi::class)
@Composable
@Preview
fun App() {
    val pedido = Pedido(id = 1, estado = "activo", metodo_pago = "no definido", total_pago = 0.0f,)

    MaterialTheme {
        Navigator(screen = InterfazDeUsuario(pedido, 0, emptyList())) { navigator ->
            ScaleTransition(navigator)
        }
    }
}