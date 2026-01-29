# MealPlan - Claude Code Guidelines

## Project Overview

MealPlan is an Android application for meal planning, recipe management, and wellness tracking. Built with modern Android development practices using Kotlin, Jetpack Compose, and Firebase.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Hilt dependency injection
- **Database**: Room (local), Firebase Firestore (remote)
- **Authentication**: Firebase Auth with Google Sign-In
- **Image Loading**: Coil
- **Camera**: CameraX with Accompanist Permissions
- **Navigation**: Jetpack Navigation Compose
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
app/src/main/java/com/mealplan/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   ├── mock/           # Mock data for development
│   └── repository/     # Data repositories
├── di/                 # Hilt dependency injection modules
├── domain/model/       # Domain models
├── ui/
│   ├── components/     # Reusable UI components
│   ├── navigation/     # Navigation graph
│   ├── screens/        # Feature screens
│   │   ├── auth/
│   │   ├── home/
│   │   ├── onboarding/
│   │   ├── profile/
│   │   ├── quicklog/
│   │   ├── recipe/
│   │   ├── scanner/
│   │   ├── splash/
│   │   ├── weeklyplan/
│   │   └── wellness/
│   └── theme/          # Colors, typography, theme
├── MainActivity.kt
└── MealPlanApp.kt
```

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean assembleDebug
```

## Key Dependencies

- Compose BOM: 2024.02.00
- Hilt: 2.50
- Room: 2.6.1
- CameraX: 1.3.1
- Accompanist Permissions: 0.34.0
- Firebase BOM: 32.7.2
- Coil: 2.5.0

---

# Implementation Log

## Take Photo Feature (Completed)

### Original Plan

**Goal**: Enable camera functionality in ScannerScreen and QuickLogScreen. All dependencies (CameraX, Accompanist permissions) and manifest permissions were already configured.

### Files Modified/Created

#### 1. Created: `app/src/main/java/com/mealplan/ui/components/CameraPreview.kt`
Reusable camera preview composable with:
- CameraX Preview use case for live camera feed
- ImageCapture use case to take photos
- Photos saved to `${context.externalCacheDir}/meal_images/`
- Return Uri of captured image via callback
- Capture button (circular white/green design)
- Close button to dismiss camera

```kotlin
@Composable
fun CameraPreview(
    onImageCaptured: (Uri) -> Unit,
    onError: (Exception) -> Unit,
    onClose: () -> Unit
)
```

#### 2. Modified: `app/src/main/java/com/mealplan/ui/screens/scanner/ScannerViewModel.kt`
- Added `capturedPhotoUri: Uri?` to `ScannerUiState`
- Added `onPhotoTaken(uri: Uri)` function to update state
- Added `clearPhoto()` function to reset

#### 3. Modified: `app/src/main/java/com/mealplan/ui/screens/scanner/ScannerScreen.kt`
- Added camera permission handling using Accompanist `rememberPermissionState`
- Added state to toggle between scanner UI and camera preview
- Permission rationale dialog for when user denies initially
- Removed "(Coming soon)" text from CameraScanCard
- Shows `CapturedPhotoCard` with photo thumbnail after capture
- Clear/Retake buttons for captured photos

Permission Flow:
```kotlin
val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

when {
    cameraPermissionState.status.isGranted -> // Show camera
    cameraPermissionState.status.shouldShowRationale -> // Show explanation dialog
    else -> // Request permission
}
```

#### 4. Modified: `app/src/main/java/com/mealplan/ui/screens/quicklog/QuickLogViewModel.kt`
- Added `photoUri: Uri?` to `QuickLogUiState`
- Added `onPhotoTaken(uri: Uri)` function
- Added `clearPhoto()` function

#### 5. Modified: `app/src/main/java/com/mealplan/ui/screens/quicklog/QuickLogScreen.kt`
- Added camera permission handling
- Added state for showing camera preview
- Wired up "Take a photo" card to launch camera
- Shows captured photo with Clear/Retake options

### User Flow

**Scanner Screen:**
1. User taps "Scan your fridge" card
2. Check camera permission
3. If granted: show full-screen CameraPreview
4. User takes photo
5. Photo Uri passed to ViewModel
6. Return to scanner UI with photo thumbnail shown
7. Options to Clear or Retake photo
8. (Future: AI analysis of ingredients)

**QuickLog Screen:**
1. User taps "Take a photo" card
2. Permission flow (same as above)
3. Camera preview shown
4. Photo captured and displayed
5. Options to Clear or Retake

### Verification Steps
1. Build the app: `./gradlew assembleDebug`
2. Install on device/emulator with camera
3. Navigate to Scanner screen
4. Tap "Scan your fridge" card
5. Verify permission dialog appears (first time)
6. Verify camera preview shows after granting
7. Tap capture button
8. Verify returns to scanner with photo captured
9. Test QuickLog screen camera similarly

---

## Future Enhancements

### Scanner Screen
- AI-powered ingredient recognition from photos
- Barcode/QR code scanning for packaged foods
- Integration with recipe suggestions based on detected ingredients

### QuickLog Screen
- Voice note integration (currently marked "Coming soon")
- AI meal recognition from photos
- Nutritional information extraction

### General
- Photo storage in Firebase Storage
- Photo sync across devices
- Photo history/gallery view
