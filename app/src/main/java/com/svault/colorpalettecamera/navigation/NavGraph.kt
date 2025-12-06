package com.svault.colorpalettecamera.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.svault.colorpalettecamera.ui.camera.CameraScreen
import com.svault.colorpalettecamera.ui.detail.PaletteDetailScreen
import com.svault.colorpalettecamera.ui.gallery.GalleryScreen
import com.svault.colorpalettecamera.ui.onboarding.OnboardingScreen
import com.svault.colorpalettecamera.ui.onboarding.PermissionScreen

sealed class Screen(val route: String, val title: String) {
    object Onboarding : Screen("onboarding", "Onboarding")
    object Permission : Screen("permission", "Permission")
    object Camera : Screen("camera", "Camera")
    object Gallery : Screen("gallery", "Gallery")
    object PaletteDetail : Screen("palette_detail/{paletteId}", "Palette Detail") {
        fun createRoute(paletteId: String) = "palette_detail/$paletteId"
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String,
    paddingValues: PaddingValues,
    onNavigate: (String) -> Unit
) {
    when (currentRoute) {
        Screen.Onboarding.route -> OnboardingScreen(
            onGetStarted = { onNavigate(Screen.Permission.route) },
            modifier = modifier.padding(paddingValues)
        )
        Screen.Permission.route -> PermissionScreen(
            onRequestPermissions = { onNavigate(Screen.Camera.route) },
            onSkip = { onNavigate(Screen.Camera.route) },
            modifier = modifier.padding(paddingValues)
        )
        Screen.Camera.route -> CameraScreen(modifier = modifier.padding(paddingValues))
        Screen.Gallery.route -> GalleryScreen(modifier = modifier.padding(paddingValues))
        else -> CameraScreen(modifier = modifier.padding(paddingValues))
    }
}
