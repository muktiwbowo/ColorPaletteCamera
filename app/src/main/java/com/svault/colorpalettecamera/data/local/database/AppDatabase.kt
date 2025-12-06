package com.svault.colorpalettecamera.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.svault.colorpalettecamera.data.local.converter.ColorListConverter
import com.svault.colorpalettecamera.data.local.dao.PaletteDao
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity

@Database(
    entities = [PaletteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paletteDao(): PaletteDao

    companion object {
        const val DATABASE_NAME = "color_palette_db"
    }
}
