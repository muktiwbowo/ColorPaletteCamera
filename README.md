# ColorPalette Camera

An Android camera application that captures photos and automatically extracts beautiful color palettes from each image. Perfect for designers, artists, and creatives who want to discover and save inspiring color combinations from the real world.

## Overview

ColorPalette Camera transforms the way you discover colors in your environment. Simply point your camera at any scene, and the app instantly analyzes and displays the dominant colors. Capture the moment, and you'll have both the image and its extracted color palette saved together, complete with hex codes ready to use in your designs.

## Features

### Core Features
- **Photo Capture**: Take photos using CameraX with front/back camera support
- **Gallery Picker**: Import existing photos from your device gallery
- **Color Palette Extraction**: AndroidX Palette API extracts 6 color types from images:
  - Vibrant, Light Vibrant, Dark Vibrant
  - Muted, Light Muted, Dark Muted
  - Plus dominant color identification
- **Hex Code Display**: Each color shows its hex code for easy copying
- **Gallery**: Grid view of all saved palettes with image thumbnails
- **Palette Detail**: View full palette with all extracted colors
- **Color Detail**: Individual color info with HEX, RGB, HSL values
- **Copy to Clipboard**: Tap any color to copy its hex code
- **Onboarding Flow**: Welcome screen and permission request UI

### Planned Features
- Color palette editing and customization
- Export palettes in various formats (JSON, CSS, ASE)
- Share palettes directly to design tools
- Color naming and palette organization
- Search photos by color

## Technical Stack

- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt (Dagger)
- **Camera**: CameraX API (1.4.0)
- **Color Extraction**: AndroidX Palette API (1.0.0)
- **Storage**: Room Database (2.6.1) for palette persistence
- **UI**: Jetpack Compose / Material Design 3
- **Image Loading**: Coil Compose
- **Async**: Kotlin Coroutines & Flow

## Project Structure

```
app/
├── src/main/
│   ├── java/com/svault/colorpalettecamera/
│   │   ├── ColorPaletteApp.kt       # Application class with Hilt
│   │   ├── MainActivity.kt          # Main activity with navigation
│   │   ├── ui/
│   │   │   ├── camera/              # Camera feature
│   │   │   │   ├── CameraScreen.kt       # Camera UI with preview/capture
│   │   │   │   ├── CameraViewModel.kt    # Camera state management
│   │   │   │   └── CameraPreview.kt      # CameraX preview composable
│   │   │   ├── gallery/             # Gallery feature
│   │   │   │   ├── GalleryScreen.kt      # Grid view of saved palettes
│   │   │   │   └── GalleryViewModel.kt   # Gallery state management
│   │   │   ├── detail/              # Detail screens
│   │   │   │   ├── PaletteDetailScreen.kt      # Full palette view
│   │   │   │   ├── PaletteDetailViewModel.kt   # Palette detail state
│   │   │   │   └── ColorDetailScreen.kt        # Individual color info
│   │   │   ├── onboarding/          # Onboarding flow
│   │   │   │   ├── OnboardingScreen.kt    # Welcome screen
│   │   │   │   └── PermissionScreen.kt    # Permission request screen
│   │   │   ├── components/          # Reusable UI components
│   │   │   │   ├── BottomNavBar.kt       # Bottom navigation bar
│   │   │   │   └── PaletteDisplay.kt     # Color palette display widget
│   │   │   └── theme/               # Material Design 3 theme
│   │   │       ├── Color.kt              # Blue-based color scheme
│   │   │       ├── Theme.kt              # Theme configuration
│   │   │       └── Type.kt               # Typography definitions
│   │   ├── data/
│   │   │   ├── model/               # Data models
│   │   │   │   └── ColorPalette.kt       # ColorInfo & ColorPalette models
│   │   │   ├── local/               # Local database
│   │   │   │   ├── entity/
│   │   │   │   │   └── PaletteEntity.kt  # Room entity for palettes
│   │   │   │   ├── dao/
│   │   │   │   │   └── PaletteDao.kt     # Database access object
│   │   │   │   ├── database/
│   │   │   │   │   └── AppDatabase.kt    # Room database instance
│   │   │   │   └── converter/
│   │   │   │       └── ColorListConverter.kt  # JSON type converters
│   │   │   └── repository/          # Data repositories
│   │   │       └── PaletteRepository.kt  # Palette data operations
│   │   ├── di/                      # Dependency injection
│   │   │   └── DatabaseModule.kt         # Hilt database module
│   │   ├── navigation/              # Navigation system
│   │   │   └── NavGraph.kt              # Screen routes & navigation
│   │   └── utils/                   # Utility classes
│   │       ├── ColorExtractor.kt        # Palette API color extraction
│   │       ├── PermissionUtils.kt       # Permission helpers
│   │       └── PreferencesHelper.kt     # SharedPreferences wrapper
│   └── res/
│       ├── drawable/                # Vector icons
│       │   ├── ic_camera.xml            # Camera tab icon
│       │   └── ic_gallery.xml           # Gallery tab icon
│       ├── values/                  # Resources
│       │   ├── strings.xml              # String resources
│       │   ├── colors.xml               # Color values
│       │   └── themes.xml               # App themes
│       └── mipmap-*/                # App launcher icons
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or later
- Android device or emulator with camera support

### Installation

1. Clone the repository:
```bash
git clone https://github.com/muktiwibowo/colorpalette-camera.git
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on your device or emulator

### Permissions

The app requires the following permissions:
- `CAMERA`: To access device camera for live preview and photo capture
- `READ_EXTERNAL_STORAGE`: To read captured images (API < 33)
- `READ_MEDIA_IMAGES`: To read captured images (API >= 33)
- `WRITE_EXTERNAL_STORAGE`: To save captured photos (API < 29)

## Usage

1. **Launch the app**: Complete onboarding and grant camera permissions when prompted
2. **Take a photo**:
   - Tap the camera button to capture a photo
   - Or tap the gallery icon to select an existing photo
3. **View palette**: After capture, see the extracted color palette with hex codes
4. **Save or retake**:
   - Tap "Save" to store the palette in your gallery
   - Tap "Retake" to capture a new photo
5. **Browse gallery**: Switch to Gallery tab to see all saved palettes
6. **View details**:
   - Tap any palette to see full details
   - Tap any color to see RGB, HSL values and copy hex code

## Color Extraction Algorithm

The app uses the **AndroidX Palette API** which:
- Analyzes images using quantization algorithms
- Extracts 6 distinct color profiles:
  - **Vibrant colors**: High saturation, medium brightness
  - **Muted colors**: Low saturation, medium brightness
  - **Light/Dark variants**: Different brightness levels
- Identifies the most dominant color by pixel population
- Returns hex codes, RGB values, and population counts
- Automatically handles different bitmap configurations (ARGB_8888, Hardware)

## Contact

- **Developer**: Mukti
- **Location**: Yogyakarta, Indonesia 🇮🇩
- **Project**: Part of "30 Days, 10 Apps" Android Revival Challenge

---

#30Days10Apps
