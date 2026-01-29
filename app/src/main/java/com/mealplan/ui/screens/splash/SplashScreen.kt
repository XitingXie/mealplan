package com.mealplan.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800),
        label = "alpha"
    )

    LaunchedEffect(authState) {
        delay(1500) // Show splash for at least 1.5 seconds
        when (authState) {
            AuthState.NotLoggedIn -> onNavigateToAuth()
            AuthState.NeedsOnboarding -> onNavigateToOnboarding()
            AuthState.LoggedIn -> onNavigateToHome()
            AuthState.Loading -> { /* Wait */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WarmCream,
                        SoftLavenderTint
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            // App Logo/Icon placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(SoftSageGreen, WarmPeach)
                        ),
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83C\uDF7D\uFE0F",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MealPlan",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Eat well, feel great",
                style = MaterialTheme.typography.bodyLarge,
                color = SoftGray
            )
        }
    }
}
