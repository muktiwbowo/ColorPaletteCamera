package com.svault.colorpalettecamera.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import com.svault.colorpalettecamera.data.repository.PaletteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val paletteRepository: PaletteRepository
) : ViewModel() {

    val palettes: StateFlow<List<PaletteEntity>> = paletteRepository.getAllPalettes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deletePalette(paletteId: Long) {
        viewModelScope.launch {
            paletteRepository.deletePaletteById(paletteId)
        }
    }

    fun deleteAllPalettes() {
        viewModelScope.launch {
            paletteRepository.deleteAllPalettes()
        }
    }
}
