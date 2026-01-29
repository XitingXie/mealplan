# MealPlan - Personalized Meal Planning App

A personalized meal planning Android app that helps users achieve their health goals through AI-generated weekly meal plans, daily check-ins, and smart food analysis.

## Features

- **Personalized Meal Plans**: Weekly meal plans based on your health goals, dietary restrictions, and cooking skill level
- **Easy Recipes**: Most recipes are beginner-friendly with 5-8 ingredients and under 30 minutes
- **One-Tap Check-ins**: Log meals quickly with just a tap
- **Wellness Tracking**: Track how you feel (energy, mood, digestion) - not just what you eat
- **Positive Psychology**: Focus on encouragement and progress, never guilt or punishment
- **Fridge Scanner**: Find recipes based on ingredients you have (coming soon)

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Compose
- **Authentication**: Firebase Auth (Google Sign-In)
- **Database**: Room (local) + Firebase Firestore (cloud sync)
- **Networking**: Retrofit + OkHttp
- **Async**: Kotlin Coroutines + Flow

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK with API 34

### Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project or select an existing one
3. Add an Android app with package name: `com.mealplan`
4. Download the `google-services.json` file
5. Place `google-services.json` in the `app/` directory

### Enable Google Sign-In

1. In Firebase Console, go to **Authentication** > **Sign-in method**
2. Enable **Google** as a sign-in provider
3. Add your SHA-1 fingerprint:
   ```bash
   cd android && ./gradlew signingReport
   ```
4. Copy the SHA-1 from the debug variant and add it to Firebase

### Enable Firestore

1. In Firebase Console, go to **Firestore Database**
2. Click **Create database**
3. Start in **test mode** for development
4. Choose a location close to your users

### Build and Run

1. Clone the repository
2. Open the project in Android Studio
3. Add `google-services.json` to the `app/` directory
4. Sync Gradle files
5. Run on an emulator or physical device

```bash
./gradlew assembleDebug
```

## Project Structure

```
mealplan/
├── app/
│   └── src/main/
│       ├── java/com/mealplan/
│       │   ├── di/              # Dependency Injection modules
│       │   ├── data/
│       │   │   ├── local/       # Room database
│       │   │   ├── mock/        # Sample recipe data
│       │   │   └── repository/  # Data repositories
│       │   ├── domain/
│       │   │   └── model/       # Domain models
│       │   └── ui/
│       │       ├── components/  # Reusable UI components
│       │       ├── navigation/  # Navigation setup
│       │       ├── screens/     # App screens
│       │       └── theme/       # Material 3 theme
│       └── res/                 # Resources
└── build.gradle.kts
```

## Screens

1. **Splash** - App loading and auth check
2. **Auth** - Google Sign-In
3. **Onboarding** - User preferences questionnaire
4. **Home** - Today's meal plan with quick actions
5. **Weekly Plan** - View the full week's meals
6. **Recipe Detail** - Full recipe with ingredients and instructions
7. **Scanner** - Find recipes by ingredients (placeholder)
8. **Quick Log** - Log meals quickly
9. **Profile** - User stats and settings
10. **Wellness Check-in** - Weekly wellness survey
11. **Benefits Timeline** - What to expect from healthy eating

## Design Philosophy

This app is built around positive psychology principles:

- **No weight tracking** - Focus on how you feel, not numbers
- **No guilt** - Missed a day? Just jump back in
- **Celebrate progress** - Every healthy meal counts
- **Cumulative tracking** - Total healthy days never reset

## Configuration

- Min SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 14)
- Kotlin: 1.9.22
- Compose BOM: 2024.02.00

## Future Enhancements

- [ ] Gemini API integration for food photo analysis
- [ ] Fridge scanning with ML Kit
- [ ] Push notifications for meal reminders
- [ ] Shopping list generation
- [ ] Recipe sharing
- [ ] Voice note logging

## License

This project is for educational purposes.

## Support

For issues and feature requests, please create an issue in the repository.
