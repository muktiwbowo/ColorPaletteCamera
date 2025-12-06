package com.svault.colorpalettecamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.svault.colorpalettecamera.navigation.AppNavigation
import com.svault.colorpalettecamera.navigation.Screen
import com.svault.colorpalettecamera.ui.components.BottomNavBar
import com.svault.colorpalettecamera.ui.components.BottomNavItem
import com.svault.colorpalettecamera.ui.theme.ColorPaletteCameraTheme
import com.svault.colorpalettecamera.utils.PermissionUtils
import com.svault.colorpalettecamera.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var preferencesHelper: PreferencesHelper
    private var onPermissionResult: ((Boolean) -> Unit)? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        onPermissionResult?.invoke(allGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferencesHelper = PreferencesHelper(this)

        setContent {
            ColorPaletteCameraTheme {
                val isOnboardingCompleted = preferencesHelper.isOnboardingCompleted
                val hasPermissions = PermissionUtils.hasAllPermissions(this)

                val initialRoute = when {
                    !isOnboardingCompleted -> Screen.Onboarding.route
                    !hasPermissions -> Screen.Permission.route
                    else -> Screen.Camera.route
                }

                var currentRoute by rememberSaveable { mutableStateOf(initialRoute) }

                val showBottomBar = currentRoute == Screen.Camera.route ||
                                   currentRoute == Screen.Gallery.route

                val bottomNavItems = listOf(
                    BottomNavItem(
                        title = "Camera",
                        icon = R.drawable.ic_camera,
                        route = Screen.Camera.route
                    ),
                    BottomNavItem(
                        title = "Gallery",
                        icon = R.drawable.ic_gallery,
                        route = Screen.Gallery.route
                    )
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(
                                items = bottomNavItems,
                                currentRoute = currentRoute,
                                onItemClick = { route ->
                                    currentRoute = route
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        currentRoute = currentRoute,
                        paddingValues = innerPadding,
                        onNavigate = { route ->
                            when (route) {
                                Screen.Permission.route -> {
                                    preferencesHelper.isOnboardingCompleted = true
                                    currentRoute = route
                                }
                                Screen.Camera.route -> {
                                    if (currentRoute == Screen.Permission.route) {
                                        requestPermissions { granted ->
                                            if (granted || PermissionUtils.hasAllPermissions(this)) {
                                                currentRoute = Screen.Camera.route
                                            }
                                        }
                                    } else {
                                        currentRoute = route
                                    }
                                }
                                else -> currentRoute = route
                            }
                        }
                    )
                }
            }
        }
    }

    private fun requestPermissions(onResult: (Boolean) -> Unit) {
        onPermissionResult = onResult
        permissionLauncher.launch(PermissionUtils.REQUIRED_PERMISSIONS)
    }
}