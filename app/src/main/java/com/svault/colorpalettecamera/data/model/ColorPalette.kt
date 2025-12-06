package com.svault.colorpalettecamera.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

data class ColorInfo(
    val hexCode: String,
    val name: String,
    val population: Int = 0
) {
    // Transient property not stored in database
    val color: Color
        get() = Color(hexCode.toColorInt())

    // Helper to get ARGB value
    val argb: Int
        get() = color.toArgb()
}

data class ColorPalette(
    val colors: List<ColorInfo> = emptyList(),
    val dominantColor: ColorInfo? = null
)

fun Int.toHexCode(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}

fun Int.toComposeColor(): Color {
    return Color(this)
}
