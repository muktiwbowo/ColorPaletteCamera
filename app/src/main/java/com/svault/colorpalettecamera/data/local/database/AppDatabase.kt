package com.svault.colorpalettecamera.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.svault.colorpalettecamera.data.local.converter.ColorListConverter
import com.svault.colorpalettecamera.data.local.dao.PaletteDao
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity

@Database(
    entities = [PaletteEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paletteDao(): PaletteDao

    companion object {
        const val DATABASE_NAME = "color_palette_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE palettes ADD COLUMN name TEXT")
                db.execSQL("ALTER TABLE palettes ADD COLUMN description TEXT")
            }
        }
    }
}
