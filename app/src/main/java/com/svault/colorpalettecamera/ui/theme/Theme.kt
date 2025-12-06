package com.svault.colorpalettecamera.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = BlueLight80,

    secondary = Cyan80,
    onSecondary = Color(0xFF003640),
    secondaryContainer = Color(0xFF004F5C),
    onSecondaryContainer = Color(0xFFB3E5FC),

    tertiary = BlueGrey80,
    onTertiary = Color(0xFF263238),
    tertiaryContainer = Color(0xFF37474F),
    onTertiaryContainer = Color(0xFFCFD8DC),

    background = Color(0xFF0A1929),
    onBackground = Color(0xFFE3F2FD),
    surface = Color(0xFF132F4C),
    onSurface = Color(0xFFE3F2FD),
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = Color(0xFFB3D4FF)
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E4FD),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Cyan40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB3E5FC),
    onSecondaryContainer = Color(0xFF001F24),

    tertiary = BlueGrey40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFCFD8DC),
    onTertiaryContainer = Color(0xFF1C313A),

    background = Color(0xFFFBFCFE),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = LightBlueBg,
    onSurfaceVariant = Color(0xFF42474E)
)

@Composable
fun ColorPaletteCameraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}