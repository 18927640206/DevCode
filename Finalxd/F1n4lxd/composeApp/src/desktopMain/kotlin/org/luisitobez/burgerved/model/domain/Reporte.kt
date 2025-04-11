package org.luisitobez.burgerved.model.domain

import org.luisitobez.burgerved.model.data.ProblemaPedido
import java.time.LocalDateTime

data class Reporte(
    val idPedido: Long,
    val problema: ProblemaPedido,
    val descripcion: String,
    val contactoCliente: String,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val estado: EstadoReporte = EstadoReporte.PENDIENTE
)

enum class EstadoReporte {
    PENDIENTE, RESUELTO, RECHAZADO
}
