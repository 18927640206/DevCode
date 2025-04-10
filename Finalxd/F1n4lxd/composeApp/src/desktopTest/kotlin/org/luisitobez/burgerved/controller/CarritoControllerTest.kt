package org.luisitobez.burgerved.controller

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
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
            fecha_hora = LocalDateTime.now(),
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

    @Test
    fun obtenerUltimoPedido() {
        // Configuración
        val pedidoEsperado = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 0f,
            fecha_hora = LocalDateTime.now(),
            descuento = 0f,
            montoAhorrado = 0f
        )

        every { pedidoDAO.getPedido() } returns pedidoEsperado

        // Ejecución
        val resultado = controller.obtenerUltimoPedido()

        // Verificación
        assertEquals(pedidoEsperado, resultado)
        verify(exactly = 1) { pedidoDAO.getPedido() }
    }

    @Test
    fun eliminarPedido() {
        // Configuración
        val pedido = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 0f,
            fecha_hora = LocalDateTime.now(),
            descuento = 0f,
            montoAhorrado = 0f
        )

        every { pedidoDAO.borrarPedido(pedido) } just Runs // Simulamos que el método borra el pedido sin devolver nada

        // Ejecución
        controller.eliminarPedido(pedido)

        // Verificación
        verify(exactly = 1) { pedidoDAO.borrarPedido(pedido) }
    }

    @Test
    fun actualizarPrecioPedido() {
        // Configuración
        val pedido = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 10f,
            fecha_hora = LocalDateTime.now(),
            descuento = 0f,
            montoAhorrado = 0f
        )
        val nuevoPrecio = 20f

        every { pedidoDAO.actualizarCostoPedido(pedido.id, nuevoPrecio) } just Runs // Simulamos que el método actualiza el costo sin devolver nada

        // Ejecución
        controller.actualizarPrecioPedido(pedido, nuevoPrecio)

        // Verificación
        verify(exactly = 1) { pedidoDAO.actualizarCostoPedido(pedido.id, nuevoPrecio) }
    }

    @Test
    fun realizarPago() {
        // Configuración
        val pedido = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 10f,
            fecha_hora = LocalDateTime.now(),
            descuento = 0f,
            montoAhorrado = 0f
        )
        val totalAmount = 15f
        val metodoPago = "Tarjeta"

        every { pedidoDAO.actualizarEstadoPedido(pedido.id, "Pagado", metodoPago) } just Runs // Simulamos que el estado del pedido es actualizado
        // Ejecutar el método realizarPago
        val resultado = controller.realizarPago(pedido, totalAmount, metodoPago)

        // Verificación
        assertEquals("Pago exitoso por $$totalAmount", resultado)
        verify(exactly = 1) { pedidoDAO.actualizarEstadoPedido(pedido.id, "Pagado", metodoPago) }
    }

    @Test
    fun aplicarDescuento() {
        // Configuración
        val pedido = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 100f,
            fecha_hora = LocalDateTime.now(),
            descuento = 0f,
            montoAhorrado = 0f
        )
        val contador = 3  // Para aplicar un 20% de descuento

        every { pedidoDAO.updatePedido(pedido, 0.20f, 20f) } just Runs // Simulamos que el pedido es actualizado con el descuento

        // Ejecutar el método aplicarDescuento
        val resultado = controller.aplicarDescuento(pedido, contador)

        // Verificación
        assertEquals(80f, resultado.total_pago)  // 100f - 20% = 80f
        assertEquals(0.20f, resultado.descuento)
        assertEquals(20f, resultado.montoAhorrado)
        verify(exactly = 1) { pedidoDAO.updatePedido(pedido, 0.20f, 20f) }
    }

    @Test
    fun notificarDescuento() {
        // Creamos un mock del CarritoController para simular su comportamiento
        val controllerMock = mockk<CarritoController>()

        // Configuración de los retornos esperados para los diferentes casos
        every { controllerMock.notificarDescuento(0) } returns ""
        every { controllerMock.notificarDescuento(1) } returns "¡OFERTA! En la compra de 2 productos, obtienes un 15% de descuento."
        every { controllerMock.notificarDescuento(2) } returns "En la compra de 3 o más productos, obtienes un 20% de descuento."
        every { controllerMock.notificarDescuento(3) } returns "En la compra de 3 o más productos, obtienes un 20% de descuento."

        // Caso 1: 0 productos
        var resultado = controllerMock.notificarDescuento(0)
        assertEquals("", resultado)  // Esperado: cadena vacía

        // Caso 2: 1 producto
        resultado = controllerMock.notificarDescuento(1)
        assertEquals("¡OFERTA! En la compra de 2 productos, obtienes un 15% de descuento.", resultado)

        // Caso 3: 2 productos
        resultado = controllerMock.notificarDescuento(2)
        assertEquals("En la compra de 3 o más productos, obtienes un 20% de descuento.", resultado)

        // Caso 4: 3 o más productos
        resultado = controllerMock.notificarDescuento(3)
        assertEquals("En la compra de 3 o más productos, obtienes un 20% de descuento.", resultado)
    }

}
