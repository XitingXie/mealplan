package com.mealplan.ui.screens.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mealplan.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val needsOnboarding: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository.signInWithGoogle(account)
                .onSuccess {
                    val needsOnboarding = !authRepository.hasCompletedOnboarding()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSignedIn = true,
                            needsOnboarding = needsOnboarding
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Sign-in failed"
                        )
                    }
                }
        }
    }

    fun onSignInError(message: String) {
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
    }
}
