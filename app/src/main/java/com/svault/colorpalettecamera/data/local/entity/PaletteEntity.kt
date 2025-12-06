package com.svault.colorpalettecamera.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.svault.colorpalettecamera.data.local.converter.ColorListConverter
import com.svault.colorpalettecamera.data.model.ColorInfo

@Entity(tableName = "palettes")
@TypeConverters(ColorListConverter::class)
data class PaletteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageUri: String,
    val colors: List<ColorInfo>,
    val dominantColor: ColorInfo?,
    val name: String? = null,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
