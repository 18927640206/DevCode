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
    fun `should reject blank credentials`(email: String, password: String) {
        val result = usuarioController.administrarSesion(email, password)
        assertFalse(result)
        verify(exactly = 0) { usuarioDAO.IniciarSesion(any(), any()) }
    }

    // Test cases for credential trimming
    @Test
    fun `should trim credentials before checking`() {
        // Setup
        every { usuarioDAO.IniciarSesion("test@example.com", "password123") } returns true

        // Execute with padded credentials
        val result = usuarioController.administrarSesion("  test@example.com  ", "  password123  ")

        // Verify
        assertTrue(result)
        verify(exactly = 1) {
            usuarioDAO.IniciarSesion("test@example.com", "password123")
        }
    }

    // Test cases for successful login
    @Test
    fun `should return true for valid credentials`() {
        // Setup
        val validEmail = "admin@burgerved.com"
        val validPassword = "secure123"
        every { usuarioDAO.IniciarSesion(validEmail, validPassword) } returns true

        // Execute
        val result = usuarioController.administrarSesion(validEmail, validPassword)

        // Verify
        assertTrue(result)
        verify { usuarioDAO.IniciarSesion(validEmail, validPassword) }
    }

    // Test cases for failed login
    @Test
    fun `should return false for invalid credentials`() {
        // Setup
        val invalidEmail = "wrong@example.com"
        val invalidPassword = "wrongpass"
        every { usuarioDAO.IniciarSesion(invalidEmail, invalidPassword) } returns false

        // Execute
        val result = usuarioController.administrarSesion(invalidEmail, invalidPassword)

        // Verify
        assertFalse(result)
        verify { usuarioDAO.IniciarSesion(invalidEmail, invalidPassword) }
    }

    // Test cases for exception handling
    @Test
    fun `should handle database exceptions gracefully`() {
        // Setup
        val errorEmail = "error@example.com"
        val errorPassword = "errorpass"
        every { usuarioDAO.IniciarSesion(errorEmail, errorPassword) } throws
                RuntimeException("Database connection failed")

        // Execute
        val result = usuarioController.administrarSesion(errorEmail, errorPassword)

        // Verify
        assertFalse(result)
        verify { usuarioDAO.IniciarSesion(errorEmail, errorPassword) }
    }

    // Edge case testing
    @Test
    fun `should handle SQL injection attempts`() {
        // Setup
        val sqlInjectionEmail = "admin@burgerved.com'--"
        val sqlInjectionPassword = "whatever"
        every { usuarioDAO.IniciarSesion(sqlInjectionEmail, sqlInjectionPassword) } returns false

        // Execute
        val result = usuarioController.administrarSesion(sqlInjectionEmail, sqlInjectionPassword)

        // Verify
        assertFalse(result)
        verify {
            usuarioDAO.IniciarSesion(sqlInjectionEmail, sqlInjectionPassword)
        }
    }
}