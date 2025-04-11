package org.luisitobez.burgerved.controller

import androidx.annotation.VisibleForTesting
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.luisitobez.burgerved.model.data.PedidoDAOImpl
import org.luisitobez.burgerved.model.data.PedidoProgramadoDAOImpl
import org.luisitobez.burgerved.model.domain.EstadoProgramado
import org.luisitobez.burgerved.model.domain.Pedido
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class PedidoControllerTest {

    private lateinit var pedidoDAO: PedidoDAOImpl
    private lateinit var pedidoProgramadoDAO: PedidoProgramadoDAOImpl
    private lateinit var controller: PedidoController

    @BeforeEach
    fun setUp() {
        pedidoDAO = mockk()
        pedidoProgramadoDAO = mockk()
        controller = spyk(PedidoController(pedidoDAO, pedidoProgramadoDAO), recordPrivateCalls = true)
    }

    @Test
    fun agregarPedido() {
        val pedidoEsperado = Pedido(
            id = 1,
            estado = "activo",
            metodo_pago = "sin definir",
            total_pago = 0f,
            descuento = 0f,
            montoAhorrado = 0f
        )
        every { pedidoDAO.addPedido() } returns pedidoEsperado

        val resultado = controller.agregarPedido()

        assertEquals(pedidoEsperado, resultado)
        verify(exactly = 1) { pedidoDAO.addPedido() }
    }

    @Test
    fun `programarPedido debe asignar codigo unico y guardar pedido programado`() {
        val horaRecoger = LocalDateTime.now().plusHours(1)
        val horaExpiradoExtra = horaRecoger.plusMinutes(30)
        val pedidoOriginal = Pedido(
            id = 1,
            estado = "Nuevo",
            metodo_pago = "efectivo",
            total_pago = 80.0f,
            descuento = 0f,
            montoAhorrado = 0f
        )

        val codigoGenerado = "ABC123"

        every { controller["generarCodigo"]()} returns codigoGenerado
        every { controller.verificarCodigo(codigoGenerado) } returns null
        every { pedidoProgramadoDAO.guardarPedido(any()) } returns true

        val resultado = controller.programarPedido(pedidoOriginal, horaRecoger)

        assertEquals(true, resultado.pedidoProgramado)
        assertEquals(horaRecoger, resultado.horaRecoger)
        assertEquals(horaExpiradoExtra, resultado.horaExpirado)
        assertEquals(codigoGenerado, resultado.codigoRecoger)
        assertEquals(EstadoProgramado.PENDIENTE, resultado.estadoProgramado)
        assertEquals(pedidoOriginal.id, resultado.id)

        verify(exactly = 1) { pedidoProgramadoDAO.guardarPedido(any()) }

    }

    @VisibleForTesting
    private fun generarCodigo(): String {
        val letras = "ABCDEFGHJKLMNPQRSTUVWXYZ"
        val numeros = "23456789"

        val parteLetras = (1..3).map { letras.random() }.joinToString("")
        val parteNumeros = (1..3).map { numeros.random() }.joinToString("")

        return parteLetras + parteNumeros
    }

    @Test
    fun `generarCodigo debe devolver un codigo con 3 letras seguidas de 3 numeros`() {
        val codigo = generarCodigo()

        assertTrue(codigo.length == 6, "El codigo debe tener 6 caracteres")
        assertTrue(codigo.substring(0, 3).all { it.isLetter() }, "Las primeras 3 posiciones son letras")
        assertTrue(codigo.substring(3).all { it.isDigit() }, "Las ultimas 3 posiciones son numeros")
    }

    @Test
    fun `verificarCodigo debe devolver null si no se encuentra el código`() {
        every { pedidoProgramadoDAO.recogerPedido() } returns emptyList()

        val codigo = "DEF456"
        val resultado = controller.verificarCodigo(codigo)

        assertNull(resultado)
    }

    @Test
    fun `verificarCodigo debe devolver un pedido si se encuentra un código coincidente`() {
        val pedido = Pedido(
            id = 1,
            estado = "Nuevo",
            metodo_pago = "efectivo",
            total_pago = 80.0f,
            descuento = 0f,
            montoAhorrado = 0f,
            codigoRecoger = "GHJ789",
            estadoProgramado = EstadoProgramado.PENDIENTE
        )
        every { pedidoProgramadoDAO.recogerPedido() } returns listOf(pedido)

        val codigo = "GHJ789"
        val resultado = controller.verificarCodigo(codigo)

        assertNotNull(resultado)
        assertEquals(pedido, resultado)
    }

    @Test
    fun `verificarCodigo debe devolver null si el código no está pendiente`() {
        val pedido = Pedido(
            id = 1,
            estado = "Nuevo",
            metodo_pago = "efectivo",
            total_pago = 80.0f,
            descuento = 0f,
            montoAhorrado = 0f,
            codigoRecoger = "KLM123",
            estadoProgramado = EstadoProgramado.ENTREGADO
        )
        every { pedidoProgramadoDAO.recogerPedido() } returns listOf(pedido)

        val codigo = "KLM123"
        val resultado = controller.verificarCodigo(codigo)

        assertNull(resultado)
    }

    @Test
    fun `marcarPedidoEntregado debe actualizar el estado a entregado si la entrega es exitosa`() {
        val pedido = Pedido(
            id = 1,
            estado = "Nuevo",
            metodo_pago = "efectivo",
            total_pago = 80.0f,
            descuento = 0f,
            montoAhorrado = 0f,
            codigoRecoger = "NOP456"
        )
        every { pedidoProgramadoDAO.guardarPedido(any()) } returns true

        assertDoesNotThrow { controller.marcarPedidoEntregado(pedido) }

        verify(exactly = 1) { pedidoProgramadoDAO.guardarPedido(any()) }
    }
}
