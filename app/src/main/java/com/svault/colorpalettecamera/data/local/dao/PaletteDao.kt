package com.svault.colorpalettecamera.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaletteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPalette(palette: PaletteEntity): Long

    @Update
    suspend fun updatePalette(palette: PaletteEntity)

    @Delete
    suspend fun deletePalette(palette: PaletteEntity)

    @Query("SELECT * FROM palettes ORDER BY createdAt DESC")
    fun getAllPalettes(): Flow<List<PaletteEntity>>

    @Query("SELECT * FROM palettes WHERE id = :paletteId")
    suspend fun getPaletteById(paletteId: Long): PaletteEntity?

    @Query("DELETE FROM palettes WHERE id = :paletteId")
    suspend fun deletePaletteById(paletteId: Long)

    @Query("DELETE FROM palettes")
    suspend fun deleteAllPalettes()
}
