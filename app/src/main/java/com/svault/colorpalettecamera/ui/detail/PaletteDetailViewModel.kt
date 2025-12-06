package com.svault.colorpalettecamera.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import com.svault.colorpalettecamera.data.repository.PaletteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaletteDetailViewModel @Inject constructor(
    private val paletteRepository: PaletteRepository
) : ViewModel() {

    private val _palette = MutableStateFlow<PaletteEntity?>(null)
    val palette: StateFlow<PaletteEntity?> = _palette.asStateFlow()

    private var currentPaletteId: Long = 0

    fun loadPalette(paletteId: Long) {
        currentPaletteId = paletteId
        viewModelScope.launch {
            _palette.value = paletteRepository.getPaletteById(paletteId)
        }
    }

    fun deletePalette() {
        viewModelScope.launch {
            if (currentPaletteId != 0L) {
                paletteRepository.deletePaletteById(currentPaletteId)
            }
        }
    }
}
