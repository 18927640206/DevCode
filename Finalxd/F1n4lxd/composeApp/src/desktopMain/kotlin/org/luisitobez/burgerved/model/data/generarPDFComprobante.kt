package org.luisitobez.burgerved.model.data

import com.itextpdf.text.Document
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.luisitobez.burgerved.controller.ProductoController
import org.luisitobez.burgerved.model.domain.Pedido
import java.io.File
import java.io.FileOutputStream


fun generarPdfComprobante(pedido: Pedido) {
    try {
        // Crear el documento PDF
        val document = Document()

        // Definir la ubicación del archivo PDF
        val file = File("C:\\Users\\jesus\\OneDrive\\Trimestres UAM\\recibos\\comprobante_pago.pdf")
        val outputStream = FileOutputStream(file)

        // Obtener el escritor de PDF
        PdfWriter.getInstance(document, outputStream)

        // Abrir el documento para agregar contenido
        document.open()

        // Fuente para el texto
        val tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16f)
        val normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12f)

        // Inicializar ProductoController con la conexión a la base de datos
        val conexion = ConexionDB()
        val productoController = ProductoController(
            productoDAO = ProductoDAOImpl(conexion),
            pedidoProductoDAO = PedidoProductoDAOImpl(conexion)
        )

        // Obtener los productos del pedido usando el controlador
        val productosDelPedido = productoController.pedirPedidoProductos(pedido)

        // Agrupar productos por ID para contar la cantidad y calcular precios
        val productosAgrupados = productosDelPedido
            .groupBy { it.idProducto }
            .mapNotNull { (idProducto, lista) ->
                val producto = productoController.obtenerProductoPorId(idProducto.toString())
                producto?.let {
                    val cantidad = lista.size
                    Triple(it, cantidad, lista.first().precioUnitario)
                }
            }

        // Título
        document.add(Paragraph("Comprobante de Pago", tituloFont))

        // Información del pedido
        document.add(Paragraph("ID Pedido: #${pedido.id}", normalFont))
        document.add(Paragraph("Fecha y Hora: ${pedido.fecha_hora}", normalFont))

        // Espacio adicional
        document.add(Paragraph("\n"))

        // Productos
        document.add(Paragraph("Productos:", tituloFont))

        // Mostrar productos en el PDF
        productosAgrupados.forEach { (producto, cantidad, precioUnitario) ->
            val total = "%.2f".format(precioUnitario * cantidad)
            document.add(Paragraph("${cantidad}x ${producto.nombre} - $$total", normalFont))
        }

        // Agregar espacio entre productos y el total
        document.add(Paragraph("\n"))

        // Información de pago
        document.add(Paragraph("Método de pago: ${pedido.metodo_pago}", normalFont))
        document.add(Paragraph("Descuento aplicado: $${"%.2f".format(pedido.descuento)}", normalFont))
        document.add(Paragraph("Total: $${"%.2f".format(pedido.total_pago)}", normalFont))

        // Generar el código QR (puedes personalizarlo con lo que desees, por ejemplo, el ID del pedido o un enlace de pago)
        val qrContent = "https://1drv.ms/f/c/c8261ae0e6423200/EvHvXLZonk1Pn839s4-6TzgBc0_egXbkFSVTYuZVo8x4ng?e=iC7ktc" // Un enlace o texto relacionado con el pedido
        val archivo = "codigoQR.png"
        generarCodigoQR(qrContent, archivo)

        // Cerrar el documento
        document.close()

        // Confirmar la creación del archivo
        println("PDF generado en: ${file.absolutePath}")
    } catch (e: Exception) {
        // Manejo de excepciones en caso de error
        println("Error al generar el PDF: ${e.message}")
    }
}



