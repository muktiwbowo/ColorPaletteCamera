package com.svault.colorpalettecamera.data.repository

import com.svault.colorpalettecamera.data.local.dao.PaletteDao
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaletteRepository @Inject constructor(
    private val paletteDao: PaletteDao
) {

    fun getAllPalettes(): Flow<List<PaletteEntity>> {
        return paletteDao.getAllPalettes()
    }

    suspend fun getPaletteById(paletteId: Long): PaletteEntity? {
        return paletteDao.getPaletteById(paletteId)
    }

    suspend fun insertPalette(palette: PaletteEntity): Long {
        return paletteDao.insertPalette(palette)
    }

    suspend fun updatePalette(palette: PaletteEntity) {
        paletteDao.updatePalette(palette)
    }

    suspend fun deletePalette(palette: PaletteEntity) {
        paletteDao.deletePalette(palette)
    }

    suspend fun deletePaletteById(paletteId: Long) {
        paletteDao.deletePaletteById(paletteId)
    }

    suspend fun deleteAllPalettes() {
        paletteDao.deleteAllPalettes()
    }
}
