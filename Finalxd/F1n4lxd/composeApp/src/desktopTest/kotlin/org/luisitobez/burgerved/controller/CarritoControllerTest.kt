package org.luisitobez.burgerved.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.domain.Pedido
import java.time.LocalDateTime

class CarritoControllerTest {
    // Variables para el mock de PedidoDAOImpl y la clase que vamos a probar (CarritoController)
    private lateinit var pedidoDAO: PedidoDAOImpl
    private lateinit var controller: CarritoController
    // Este metodo se ejecuta antes de cada prueba
    @BeforeEach
    fun setUp() {
        pedidoDAO = mockk()  //Se crea un mock de PedidoDAOImpl
        controller = CarritoController(pedidoDAO) // Creamos una instancia del controlador con el mock
    }

    @Test
    fun agregarPedido() {
        // Configuración
        val pedidoEsperado = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 0f,
            descuento = 0f,
            montoAhorrado = 0f
        )

        every { pedidoDAO.addPedido() } returns pedidoEsperado

        // Ejecución
        val resultado = controller.agregarPedido()

        // Verificación
        assertEquals(pedidoEsperado, resultado)
        verify(exactly = 1) { pedidoDAO.addPedido() }
    }
}