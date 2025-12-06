package com.svault.colorpalettecamera.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.svault.colorpalettecamera.data.model.ColorInfo
import com.svault.colorpalettecamera.data.model.ColorPalette

@Composable
fun PaletteDisplay(
    palette: ColorPalette,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Title
            Text(
                text = "Color Palette",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dominant Color
            palette.dominantColor?.let { dominantColor ->
                ColorItem(
                    colorInfo = dominantColor,
                    isLarge = true,
                    onColorClick = {
                        copyToClipboard(context, dominantColor.hexCode)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Color List
            if (palette.colors.isNotEmpty()) {
                Text(
                    text = "Extracted Colors",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(palette.colors) { colorInfo ->
                        ColorCard(
                            colorInfo = colorInfo,
                            onClick = {
                                copyToClipboard(context, colorInfo.hexCode)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hint Text
            Text(
                text = "Tap any color to copy hex code",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ColorItem(
    colorInfo: ColorInfo,
    isLarge: Boolean = false,
    onColorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .clickable(onClick = onColorClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color Box
        Box(
            modifier = Modifier
                .size(if (isLarge) 64.dp else 48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorInfo.color)
                .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Color Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = colorInfo.name,
                fontSize = if (isLarge) 18.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = colorInfo.hexCode,
                fontSize = if (isLarge) 16.sp else 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ColorCard(
    colorInfo: ColorInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Color Box
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorInfo.color)
                .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Color Name
        Text(
            text = colorInfo.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Hex Code
        Text(
            text = colorInfo.hexCode,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Color Hex Code", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied $text to clipboard", Toast.LENGTH_SHORT).show()
}
