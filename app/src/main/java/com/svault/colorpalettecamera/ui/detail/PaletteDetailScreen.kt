package com.svault.colorpalettecamera.ui.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.svault.colorpalettecamera.data.model.ColorInfo
import com.svault.colorpalettecamera.ui.components.EditPaletteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaletteDetailScreen(
    paletteId: Long,
    onBackClick: () -> Unit,
    onColorClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaletteDetailViewModel = hiltViewModel()
) {
    val palette by viewModel.palette.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        EditPaletteDialog(
            currentName = palette?.name,
            currentDescription = palette?.description,
            onDismiss = { showEditDialog = false },
            onSave = { name, description ->
                viewModel.updatePalette(name, description)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Palette Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.deletePalette()
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        palette?.let { paletteEntity ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(paletteEntity.imageUri)),
                            contentDescription = "Palette Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Name and Description
                if (paletteEntity.name != null || paletteEntity.description != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                paletteEntity.name?.let { name ->
                                    Text(
                                        text = name,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                paletteEntity.description?.let { description ->
                                    if (paletteEntity.name != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    Text(
                                        text = description,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Dominant Color
                paletteEntity.dominantColor?.let { dominant ->
                    item {
                        DominantColorCard(
                            colorInfo = dominant,
                            onClick = { onColorClick(dominant.hexCode) }
                        )
                    }
                }

                // All Colors Header
                item {
                    Text(
                        text = "All Colors (${paletteEntity.colors.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Color List
                items(paletteEntity.colors) { colorInfo ->
                    ColorListItem(
                        colorInfo = colorInfo,
                        onClick = { onColorClick(colorInfo.hexCode) },
                        onRemove = if (paletteEntity.colors.size > 1) {
                            { viewModel.removeColor(colorInfo) }
                        } else null
                    )
                }
            }
        }
    }

    // Load palette when screen is created
    LaunchedEffect(paletteId) {
        viewModel.loadPalette(paletteId)
    }
}

@Composable
fun DominantColorCard(
    colorInfo: ColorInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorInfo.color)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dominant Color",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = colorInfo.hexCode,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = colorInfo.name,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ColorListItem(
    colorInfo: ColorInfo,
    onClick: () -> Unit,
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Color Circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(colorInfo.color)
            )

            // Color Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = colorInfo.hexCode,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = colorInfo.name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (colorInfo.population > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Population: ${colorInfo.population}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Remove Button
            onRemove?.let { removeAction ->
                IconButton(onClick = removeAction) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove color",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
