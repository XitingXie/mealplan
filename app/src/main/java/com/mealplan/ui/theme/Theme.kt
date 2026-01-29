package com.mealplan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SoftSageGreen,
    onPrimary = White,
    primaryContainer = SoftSageGreen.copy(alpha = 0.2f),
    onPrimaryContainer = SoftSageGreenDark,

    secondary = WarmPeach,
    onSecondary = WarmCharcoal,
    secondaryContainer = WarmPeach.copy(alpha = 0.3f),
    onSecondaryContainer = WarmCharcoal,

    tertiary = SoftCoral,
    onTertiary = White,
    tertiaryContainer = SoftCoral.copy(alpha = 0.2f),
    onTertiaryContainer = WarmCharcoal,

    background = WarmCream,
    onBackground = WarmCharcoal,

    surface = SoftWhite,
    onSurface = WarmCharcoal,
    surfaceVariant = SoftLavenderTint,
    onSurfaceVariant = SoftGray,

    error = SoftCoral,
    onError = White,
    errorContainer = SoftCoral.copy(alpha = 0.2f),
    onErrorContainer = WarmCharcoal,

    outline = LightGray,
    outlineVariant = LightGray.copy(alpha = 0.5f),

    scrim = TransparentBlack20
)

private val DarkColorScheme = darkColorScheme(
    primary = MutedSage,
    onPrimary = SoftDark,
    primaryContainer = MutedSage.copy(alpha = 0.2f),
    onPrimaryContainer = MutedSage,

    secondary = WarmPeach.copy(alpha = 0.8f),
    onSecondary = SoftDark,
    secondaryContainer = WarmPeach.copy(alpha = 0.2f),
    onSecondaryContainer = WarmPeach,

    tertiary = SoftCoral.copy(alpha = 0.8f),
    onTertiary = SoftDark,
    tertiaryContainer = SoftCoral.copy(alpha = 0.2f),
    onTertiaryContainer = SoftCoral,

    background = SoftDark,
    onBackground = SoftWhite,

    surface = MutedNavy,
    onSurface = SoftWhite,
    surfaceVariant = MutedNavy.copy(alpha = 0.8f),
    onSurfaceVariant = LightGray,

    error = SoftCoral.copy(alpha = 0.8f),
    onError = SoftDark,
    errorContainer = SoftCoral.copy(alpha = 0.2f),
    onErrorContainer = SoftCoral,

    outline = SoftGray,
    outlineVariant = SoftGray.copy(alpha = 0.5f),

    scrim = TransparentBlack20
)

@Composable
fun MealPlanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom calm colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
