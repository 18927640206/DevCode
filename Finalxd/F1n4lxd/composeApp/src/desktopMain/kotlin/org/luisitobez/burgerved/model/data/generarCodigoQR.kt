package org.luisitobez.burgerved.model.data

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

// Function para generar el código QR y devolverlo como una imagen en formato ByteArray
fun generarCodigoQR(texto: String, archivoSalida: String) {
    val qrCodeWriter = QRCodeWriter()
    val hints = hashMapOf<EncodeHintType, Any>()
    hints[EncodeHintType.MARGIN] = 1  // Márgenes del código QR

    // Generar el BitMatrix (matriz de bits)
    val bitMatrix: BitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 200, 200, hints)

    // Convertir el BitMatrix a un array de bytes (imagen en formato PNG)
    val byteArrayOutputStream = ByteArrayOutputStream()

    // Crear una imagen desde el BitMatrix
    val bufferedImage = BufferedImage(bitMatrix.width, bitMatrix.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until bitMatrix.width) {
        for (y in 0 until bitMatrix.height) {
            bufferedImage.setRGB(x, y, if (bitMatrix.get(x, y)) 0x000000 else 0xFFFFFF) // Pixel negro o blanco
        }
    }
    // Guardar la imagen en un archivo PNG
    try {
        val outputFile = File("C:\\Users\\jesus\\OneDrive\\Escritorio\\DevCode\\Finalxd\\F1n4lxd\\composeApp\\src\\commonMain\\composeResources\\drawable\\${archivoSalida}")
        ImageIO.write(bufferedImage, "PNG", outputFile)
        println("Imagen guardada en: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    // Escribir la imagen en el ByteArrayOutputStream
    ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream)
}