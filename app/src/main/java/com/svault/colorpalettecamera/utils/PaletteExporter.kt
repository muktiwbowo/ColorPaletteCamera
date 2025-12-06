package com.svault.colorpalettecamera.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import com.svault.colorpalettecamera.data.model.ColorInfo
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

object PaletteExporter {

    enum class ExportFormat(val extension: String, val mimeType: String) {
        JSON("json", "application/json"),
        CSS("css", "text/css"),
        ASE("ase", "application/octet-stream"),
        TXT("txt", "text/plain")
    }

    /**
     * Export palette to JSON format
     * Compatible with design tools and web applications
     */
    fun exportToJson(palette: PaletteEntity): String {
        val json = JSONObject()

        json.put("id", palette.id)
        json.put("name", palette.name ?: "Untitled Palette")
        json.put("description", palette.description ?: "")
        json.put("createdAt", palette.createdAt)

        val colorsArray = JSONArray()
        palette.colors.forEach { color ->
            val colorObj = JSONObject()
            colorObj.put("hex", color.hexCode)
            colorObj.put("name", color.name)
            colorObj.put("rgb", getRGBObject(color))
            colorObj.put("hsl", getHSLObject(color))
            colorObj.put("population", color.population)
            colorsArray.put(colorObj)
        }
        json.put("colors", colorsArray)

        palette.dominantColor?.let { dominant ->
            val dominantObj = JSONObject()
            dominantObj.put("hex", dominant.hexCode)
            dominantObj.put("name", dominant.name)
            dominantObj.put("rgb", getRGBObject(dominant))
            json.put("dominantColor", dominantObj)
        }

        return json.toString(2) // Pretty print with 2 space indent
    }

    /**
     * Export palette to CSS format
     * CSS custom properties (variables) ready to use
     */
    fun exportToCSS(palette: PaletteEntity): String {
        val sb = StringBuilder()
        val paletteName = palette.name?.replace(" ", "-")?.lowercase() ?: "palette"

        sb.appendLine("/* ${palette.name ?: "Color Palette"} */")
        palette.description?.let {
            sb.appendLine("/* $it */")
        }
        sb.appendLine()
        sb.appendLine(":root {")

        palette.colors.forEachIndexed { index, color ->
            val varName = color.name.replace(" ", "-").lowercase()
            sb.appendLine("  --$paletteName-$varName: ${color.hexCode};")
        }

        palette.dominantColor?.let { dominant ->
            sb.appendLine("  --$paletteName-dominant: ${dominant.hexCode};")
        }

        sb.appendLine("}")
        sb.appendLine()

        // Also add as class selectors
        sb.appendLine("/* Color classes */")
        palette.colors.forEach { color ->
            val className = color.name.replace(" ", "-").lowercase()
            sb.appendLine(".$paletteName-$className { color: ${color.hexCode}; }")
            sb.appendLine(".$paletteName-$className-bg { background-color: ${color.hexCode}; }")
        }

        return sb.toString()
    }

    /**
     * Export palette to Adobe Swatch Exchange (ASE) format
     * Binary format used by Adobe products
     */
    fun exportToASE(palette: PaletteEntity): ByteArray {
        val outputStream = java.io.ByteArrayOutputStream()

        // ASE file signature
        outputStream.write("ASEF".toByteArray())

        // Version (1.0)
        outputStream.write(byteArrayOf(0x00, 0x01, 0x00, 0x00))

        // Number of blocks (colors)
        val numColors = palette.colors.size
        outputStream.write(byteArrayOf(
            (numColors shr 24).toByte(),
            (numColors shr 16).toByte(),
            (numColors shr 8).toByte(),
            numColors.toByte()
        ))

        // Write each color as a block
        palette.colors.forEach { color ->
            writeASEColorBlock(outputStream, color)
        }

        return outputStream.toByteArray()
    }

    /**
     * Export palette to plain text format
     * Simple list of hex codes
     */
    fun exportToTXT(palette: PaletteEntity): String {
        val sb = StringBuilder()

        sb.appendLine(palette.name ?: "Color Palette")
        palette.description?.let {
            sb.appendLine(it)
        }
        sb.appendLine()
        sb.appendLine("Colors:")
        sb.appendLine("-------")

        palette.colors.forEach { color ->
            sb.appendLine("${color.name}: ${color.hexCode}")
        }

        palette.dominantColor?.let { dominant ->
            sb.appendLine()
            sb.appendLine("Dominant: ${dominant.hexCode}")
        }

        return sb.toString()
    }

    /**
     * Save exported content to file and return share intent
     */
    fun createShareIntent(
        context: Context,
        palette: PaletteEntity,
        format: ExportFormat
    ): Intent {
        val fileName = "${palette.name?.replace(" ", "_") ?: "palette_${palette.id}"}.${format.extension}"
        val content = when (format) {
            ExportFormat.JSON -> exportToJson(palette)
            ExportFormat.CSS -> exportToCSS(palette)
            ExportFormat.TXT -> exportToTXT(palette)
            ExportFormat.ASE -> null // Binary format handled separately
        }

        val file = File(context.cacheDir, fileName)

        if (format == ExportFormat.ASE) {
            // Write binary ASE file
            FileOutputStream(file).use { fos ->
                fos.write(exportToASE(palette))
            }
        } else {
            // Write text file
            file.writeText(content ?: "")
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return Intent(Intent.ACTION_SEND).apply {
            type = format.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, palette.name ?: "Color Palette")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    // Helper functions

    private fun getRGBObject(color: ColorInfo): JSONObject {
        val rgb = JSONObject()
        val argb = color.argb
        rgb.put("r", (argb shr 16) and 0xFF)
        rgb.put("g", (argb shr 8) and 0xFF)
        rgb.put("b", argb and 0xFF)
        return rgb
    }

    private fun getHSLObject(color: ColorInfo): JSONObject {
        val hsl = JSONObject()
        val argb = color.argb
        val r = ((argb shr 16) and 0xFF) / 255f
        val g = ((argb shr 8) and 0xFF) / 255f
        val b = (argb and 0xFF) / 255f

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        var h = 0f
        val s: Float
        val l = (max + min) / 2

        if (delta != 0f) {
            s = if (l < 0.5f) delta / (max + min) else delta / (2 - max - min)

            h = when (max) {
                r -> ((g - b) / delta + (if (g < b) 6 else 0))
                g -> ((b - r) / delta + 2)
                b -> ((r - g) / delta + 4)
                else -> 0f
            }
            h /= 6f
        } else {
            s = 0f
        }

        hsl.put("h", (h * 360).toInt())
        hsl.put("s", (s * 100).toInt())
        hsl.put("l", (l * 100).toInt())
        return hsl
    }

    private fun writeASEColorBlock(outputStream: java.io.ByteArrayOutputStream, color: ColorInfo) {
        // Block type (0x0001 = Color)
        outputStream.write(byteArrayOf(0x00, 0x01))

        // Color name length (UTF-16)
        val colorName = color.name
        val nameLength = colorName.length + 1 // +1 for null terminator
        outputStream.write(byteArrayOf(
            (nameLength shr 8).toByte(),
            nameLength.toByte()
        ))

        // Color name (UTF-16 Big Endian)
        colorName.forEach { char ->
            outputStream.write(byteArrayOf(0x00, char.code.toByte()))
        }
        outputStream.write(byteArrayOf(0x00, 0x00)) // Null terminator

        // Color model ('RGB ')
        outputStream.write("RGB ".toByteArray())

        // RGB values (32-bit float, 0.0 to 1.0)
        val argb = color.argb
        val r = ((argb shr 16) and 0xFF) / 255f
        val g = ((argb shr 8) and 0xFF) / 255f
        val b = (argb and 0xFF) / 255f

        outputStream.write(floatToBytes(r))
        outputStream.write(floatToBytes(g))
        outputStream.write(floatToBytes(b))

        // Color type (0x0002 = Global)
        outputStream.write(byteArrayOf(0x00, 0x02))
    }

    private fun floatToBytes(value: Float): ByteArray {
        val bits = value.toBits()
        return byteArrayOf(
            (bits shr 24).toByte(),
            (bits shr 16).toByte(),
            (bits shr 8).toByte(),
            bits.toByte()
        )
    }
}
