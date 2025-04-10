package org.luisitobez.burgerved.controller

import io.mockk.*
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.luisitobez.burgerved.model.data.UsuarioDAOImpl

class UsuarioControllerTest {

    // Mocks
    private lateinit var usuarioDAO: UsuarioDAOImpl
    private lateinit var usuarioController: UsuarioController

    @BeforeEach
    fun setUp() {
        usuarioDAO = mockk(relaxed = true) // relaxed mock for simpler setup
        usuarioController = UsuarioController(usuarioDAO)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll() // Clean up all mocks after each test
    }

    // Test cases for blank/empty credentials
    @ParameterizedTest
    @CsvSource(
        "'', 'password123'",       // Empty email
        "' ', 'password123'",      // Whitespace email
        "'test@example.com', ''",  // Empty password
        "'test@example.com', ' '", // Whitespace password
        "'', ''"                  // Both empty
    )
    fun VaciasOEspacios(email: String, password: String) {
        val result = usuarioController.administrarSesion(email, password)
        Assertions.assertFalse(result)
        verify(exactly = 0) { usuarioDAO.IniciarSesion(any(), any()) }
    }

    // Test cases for credential trimming
    @Test
    fun EliminarEspacios() {
        // Setup
        every { usuarioDAO.IniciarSesion("test@example.com", "password123") } returns true

        // Execute with padded credentials
        val result = usuarioController.administrarSesion("  test@example.com  ", "  password123  ")

        // Verify
        Assertions.assertTrue(result)
        verify(exactly = 1) {
            usuarioDAO.IniciarSesion("test@example.com", "password123")
        }
    }

    // Test cases for successful login
    @Test
    fun InicioExitoso() {
        // Setup
        val validEmail = "admin@burgerved.com"
        val validPassword = "secure123"
        every { usuarioDAO.IniciarSesion(validEmail, validPassword) } returns true

        // Execute
        val result = usuarioController.administrarSesion(validEmail, validPassword)

        // Verify
        Assertions.assertTrue(result)
        verify { usuarioDAO.IniciarSesion(validEmail, validPassword) }
    }

    // Test cases for failed login
    @Test
    fun InicioInvalido() {
        // Setup
        val invalidEmail = "wrong@example.com"
        val invalidPassword = "wrongpass"
        every { usuarioDAO.IniciarSesion(invalidEmail, invalidPassword) } returns false

        // Execute
        val result = usuarioController.administrarSesion(invalidEmail, invalidPassword)

        // Verify
        Assertions.assertFalse(result)
        verify { usuarioDAO.IniciarSesion(invalidEmail, invalidPassword) }
    }
}