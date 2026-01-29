package com.mealplan.ui.screens.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.mealplan.ui.components.LoadingButton
import com.mealplan.ui.theme.*

@Composable
fun AuthScreen(
    onSignInSuccess: (needsOnboarding: Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            viewModel.signInWithGoogle(account)
        } catch (e: ApiException) {
            Log.e(
                "AuthScreen",
                "Google sign-in failed: resultCode=${result.resultCode}",
                e
            )
            viewModel.onSignInError(
                "Google sign-in failed: resultCode=${result.resultCode}, " +
                    "statusCode=${e.statusCode}"
            )
        }
    }

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            onSignInSuccess(uiState.needsOnboarding)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WarmCream, SoftLavenderTint)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(SoftSageGreen, WarmPeach)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83C\uDF7D\uFE0F",
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome to MealPlan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WarmCharcoal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your personal meal planning companion.\nEat well, feel great, and build healthy habits.",
                style = MaterialTheme.typography.bodyLarge,
                color = SoftGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Sign-in button
            LoadingButton(
                text = "Continue with Google",
                isLoading = uiState.isLoading,
                onClick = {
                    val signInIntent = viewModel.getGoogleSignInIntent()
                    launcher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Error message
            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftCoral,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
