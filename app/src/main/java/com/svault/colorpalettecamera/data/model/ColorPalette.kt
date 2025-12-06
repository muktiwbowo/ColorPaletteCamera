package com.svault.colorpalettecamera.data.model

import androidx.compose.ui.graphics.Color

data class ColorInfo(
    val color: Color,
    val hexCode: String,
    val name: String,
    val population: Int = 0
)

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
