package com.svault.colorpalettecamera.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.palette.graphics.Palette
import com.svault.colorpalettecamera.data.model.ColorInfo
import com.svault.colorpalettecamera.data.model.ColorPalette
import com.svault.colorpalettecamera.data.model.toComposeColor
import com.svault.colorpalettecamera.data.model.toHexCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ColorExtractor {

    suspend fun extractPalette(context: Context, uri: Uri): ColorPalette? {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = loadBitmap(context, uri)
                val palette = Palette.from(bitmap).generate()

                val colors = mutableListOf<ColorInfo>()

                // Extract vibrant colors
                palette.vibrantSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Vibrant",
                            population = swatch.population
                        )
                    )
                }

                palette.lightVibrantSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Light Vibrant",
                            population = swatch.population
                        )
                    )
                }

                palette.darkVibrantSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Dark Vibrant",
                            population = swatch.population
                        )
                    )
                }

                // Extract muted colors
                palette.mutedSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Muted",
                            population = swatch.population
                        )
                    )
                }

                palette.lightMutedSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Light Muted",
                            population = swatch.population
                        )
                    )
                }

                palette.darkMutedSwatch?.let { swatch ->
                    colors.add(
                        ColorInfo(
                            color = swatch.rgb.toComposeColor(),
                            hexCode = swatch.rgb.toHexCode(),
                            name = "Dark Muted",
                            population = swatch.population
                        )
                    )
                }

                // Get dominant color
                val dominantSwatch = palette.dominantSwatch
                val dominantColor = dominantSwatch?.let {
                    ColorInfo(
                        color = it.rgb.toComposeColor(),
                        hexCode = it.rgb.toHexCode(),
                        name = "Dominant",
                        population = it.population
                    )
                }

                ColorPalette(
                    colors = colors,
                    dominantColor = dominantColor
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @SuppressLint("NewApi")
    private fun loadBitmap(context: Context, uri: Uri): Bitmap {
        val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.setTargetSampleSize(2) // Reduce size for performance
                // Force ARGB_8888 config to avoid HARDWARE bitmap
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        // Ensure bitmap is in ARGB_8888 format (Palette API requirement)
        return if (originalBitmap.config == Bitmap.Config.HARDWARE) {
            val softwareBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
            originalBitmap.recycle()
            softwareBitmap
        } else if (originalBitmap.config != Bitmap.Config.ARGB_8888) {
            val convertedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
            originalBitmap.recycle()
            convertedBitmap
        } else {
            originalBitmap
        }
    }
}
