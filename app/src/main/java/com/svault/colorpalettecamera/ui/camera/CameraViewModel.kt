package com.svault.colorpalettecamera.ui.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.colorpalettecamera.data.local.entity.PaletteEntity
import com.svault.colorpalettecamera.data.model.ColorPalette
import com.svault.colorpalettecamera.data.repository.PaletteRepository
import com.svault.colorpalettecamera.utils.ColorExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class CameraUiState(
    val capturedImageUri: Uri? = null,
    val isImageCaptured: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val colorPalette: ColorPalette? = null,
    val isExtractingPalette: Boolean = false,
    val isSaving: Boolean = false
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val paletteRepository: PaletteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to start camera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePicture(context: Context) {
        val imageCapture = imageCapture ?: return

        _uiState.value = _uiState.value.copy(isLoading = true)

        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ColorPaletteCamera")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Photo capture failed: ${exc.message}"
                    )
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    _uiState.value = _uiState.value.copy(
                        capturedImageUri = output.savedUri,
                        isImageCaptured = true,
                        isLoading = false,
                        error = null
                    )

                    // Extract color palette
                    output.savedUri?.let { uri ->
                        extractColorPalette(context, uri)
                    }
                }
            }
        )
    }

    fun setImageFromGallery(context: Context, uri: Uri) {
        _uiState.value = _uiState.value.copy(
            capturedImageUri = uri,
            isImageCaptured = true,
            error = null
        )

        // Extract color palette
        extractColorPalette(context, uri)
    }

    private fun extractColorPalette(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExtractingPalette = true)

            val palette = ColorExtractor.extractPalette(context, uri)

            _uiState.value = _uiState.value.copy(
                colorPalette = palette,
                isExtractingPalette = false
            )
        }
    }

    fun retakePicture() {
        _uiState.value = _uiState.value.copy(
            capturedImageUri = null,
            isImageCaptured = false,
            error = null,
            colorPalette = null
        )
    }

    fun saveImage() {
        viewModelScope.launch {
            val imageUri = _uiState.value.capturedImageUri
            val palette = _uiState.value.colorPalette

            if (imageUri != null && palette != null) {
                _uiState.value = _uiState.value.copy(isSaving = true)

                try {
                    val paletteEntity = PaletteEntity(
                        imageUri = imageUri.toString(),
                        colors = palette.colors,
                        dominantColor = palette.dominantColor
                    )

                    paletteRepository.insertPalette(paletteEntity)

                    _uiState.value = _uiState.value.copy(
                        capturedImageUri = null,
                        isImageCaptured = false,
                        error = null,
                        colorPalette = null,
                        isSaving = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to save palette: ${e.message}",
                        isSaving = false
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    capturedImageUri = null,
                    isImageCaptured = false,
                    error = null,
                    colorPalette = null
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }
}
