# ColorPalette Camera

An Android camera application that captures photos and automatically extracts beautiful color palettes from each image. Perfect for designers, artists, and creatives who want to discover and save inspiring color combinations from the real world.

## Overview

ColorPalette Camera transforms the way you discover colors in your environment. Simply point your camera at any scene, and the app instantly analyzes and displays the dominant colors. Capture the moment, and you'll have both the image and its extracted color palette saved together, complete with hex codes ready to use in your designs.

## Features

### Core Features
- **Real-time Color Detection**: Point your camera at anything and see dominant colors extracted instantly from the live camera feed
- **Photo Capture**: Take photos and save both the image and its color palette together
- **Color Palette Extraction**: Advanced algorithm extracts beautiful, dominant colors from images
- **Hex Code Display**: Each color in the palette shows its hex code for easy use in design tools
- **Gallery**: Browse previously captured photos with their associated color palettes

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
- **Camera**: CameraX API
- **Color Extraction**: Custom algorithm or Palette API
- **Storage**: Room Database for palette persistence
- **UI**: Jetpack Compose / Material Design 3

## Project Structure

```
app/
├── src/main/
│   ├── java/com/yourpackage/colorpalettecamera/
│   │   ├── ui/
│   │   │   ├── camera/          # Camera screen and preview
│   │   │   ├── gallery/         # Gallery and saved palettes
│   │   │   └── components/      # Reusable UI components
│   │   ├── data/
│   │   │   ├── model/           # Data models
│   │   │   ├── repository/      # Data repositories
│   │   │   └── database/        # Room database
│   │   ├── domain/
│   │   │   └── usecase/         # Business logic
│   │   └── utils/
│   │       └── ColorExtractor   # Color palette extraction logic
│   └── res/
│       ├── layout/              # XML layouts (if using Views)
│       ├── drawable/            # Icons and images
│       └── values/              # Strings, colors, themes
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or later
- Android device or emulator with camera support

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/colorpalette-camera.git
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

1. **Launch the app**: Grant camera permissions when prompted
2. **Point your camera**: Aim at any scene or object
3. **View live colors**: See the dominant colors appear in real-time
4. **Capture**: Tap the capture button to save the photo and palette
5. **Browse gallery**: Access your saved photos and palettes
6. **Copy hex codes**: Tap any color to copy its hex code to clipboard

## Color Extraction Algorithm

The app uses an intelligent color extraction algorithm that:
- Analyzes the image using k-means clustering or similar techniques
- Identifies dominant and complementary colors
- Filters out similar colors for palette diversity
- Ensures colors are vibrant and usable for design purposes
- Generates 5-8 colors per image (configurable)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Android CameraX library
- Material Design guidelines
- Color theory resources

## Contact

- **Developer**: Mukti
- **Location**: Yogyakarta, Indonesia 🇮🇩
- **Project**: Part of "30 Days, 10 Apps" Android Revival Challenge

---

#30Days10Apps
