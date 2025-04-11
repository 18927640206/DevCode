package org.luisitobez.burgerved.model.domain;

import java.time.LocalDateTime

data class Pedido(
    var id: Int,
    var estado: String,
    var metodo_pago: String,
    var total_pago: Float,
    var fecha_hora: LocalDateTime,
    var descuento: Float = 0f,
    var montoAhorrado: Float = 0f,
    var pedidoProgramado: Boolean = false,
    var horaRecoger: LocalDateTime? = null,
    var codigoRecoger: String? = null,
    var estadoProgramado: EstadoProgramado = EstadoProgramado.PENDIENTE,
    var horaExpirado: LocalDateTime? = null
)

enum class EstadoProgramado {
    PENDIENTE, ENTREGADO, EXPIRADO
}