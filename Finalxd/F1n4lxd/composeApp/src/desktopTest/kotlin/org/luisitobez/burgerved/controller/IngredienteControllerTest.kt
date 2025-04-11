package org.luisitobez.burgerved.controller

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.luisitobez.burgerved.model.data.IngredienteDAOImpl
import org.luisitobez.burgerved.model.domain.Detalles
import org.luisitobez.burgerved.model.domain.Ingrediente

class IngredienteControllerTest {
 @MockK
 private lateinit var ingredienteDAO: IngredienteDAOImpl
 private lateinit var controller: IngredienteController

 @BeforeEach
 fun setUp() {
  MockKAnnotations.init(this, relaxUnitFun = true) // Configuración inicial de MockK
  // Configuración base para todos los tests
  every { ingredienteDAO.getNumeroDeIngredientes() } returns Detalles(0, 0)
  every { ingredienteDAO.getProductoById(any()) } returns null
  controller = IngredienteController(ingredienteDAO)
 }

 @AfterEach
 fun tearDown() {
  unmockkAll() // Limpieza después de cada test
 }

 @Test
 fun `obtenerTodosIngrediente retorna lista vacia cuando no hay ingredientes`() {
  // Configuración específica para este test
  every { ingredienteDAO.getNumeroDeIngredientes() } returns Detalles(
   numeroDeProductos = 0,
   numeroDeIngredientes = 0
  )

  val resultado = controller.obtenerTodosIngrediente()

  assertTrue(resultado.isEmpty())
 }

 @Test
 fun `obtenerTodosIngrediente retorna lista con ingredientes correctamente`() {
  // Datos de prueba
  val ingrediente1 = Ingrediente(
   idIng = 1,
   nombre = "Pan",
   precio = 1.0f,
   stock = 10
  )

  val ingrediente2 = Ingrediente(
   idIng = 2,
   nombre = "Carne",
   precio = 2.5f,
   stock = 5
  )

  // Configuración específica para este test
  every { ingredienteDAO.getNumeroDeIngredientes() } returns Detalles(
   numeroDeProductos = 0,
   numeroDeIngredientes = 2
  )
  every { ingredienteDAO.getProductoById("1") } returns ingrediente1
  every { ingredienteDAO.getProductoById("2") } returns ingrediente2

  val resultado = controller.obtenerTodosIngrediente()

  // Verificaciones
  assertEquals(2, resultado.size)
  assertEquals(1, resultado[0].idIng)
  assertEquals("Pan", resultado[0].nombre)
  assertEquals(2.5f, resultado[1].precio)
 }

 @Test
 fun `obtenerTodosIngrediente ignora ingredientes nulos`() {
  // Configuración específica para este test
  every { ingredienteDAO.getNumeroDeIngredientes() } returns Detalles(
   numeroDeProductos = 0,
   numeroDeIngredientes = 2
  )
  every { ingredienteDAO.getProductoById("1") } returns Ingrediente(1, "Queso", 1.5f, 8)
  every { ingredienteDAO.getProductoById("2") } returns null

  val resultado = controller.obtenerTodosIngrediente()

  assertEquals(1, resultado.size)
  assertEquals("Queso", resultado[0].nombre)
 }

 @Test
 fun `obtenerTodosIngrediente maneja correctamente cuando getNumeroDeIngredientes es null`() {
  // Configuración específica para este test
  every { ingredienteDAO.getNumeroDeIngredientes() } returns null

  val resultado = controller.obtenerTodosIngrediente()

  assertTrue(resultado.isEmpty())
 }
}