package org.luisitobez.burgerved

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.luisitobez.burgerved.controller.AppController
import org.luisitobez.burgerved.model.domain.Pedido
import org.luisitobez.burgerved.view.InterfazDeUsuario

@OptIn(ExperimentalStdlibApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = InterfazDeUsuario()) { navigator ->
            ScaleTransition(navigator)
        }
    }
}