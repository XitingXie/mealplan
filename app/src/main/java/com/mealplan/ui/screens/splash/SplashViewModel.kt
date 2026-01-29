package com.mealplan.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplan.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthState {
    Loading,
    NotLoggedIn,
    NeedsOnboarding,
    LoggedIn
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            if (!authRepository.isLoggedIn) {
                _authState.value = AuthState.NotLoggedIn
            } else if (!authRepository.hasCompletedOnboarding()) {
                _authState.value = AuthState.NeedsOnboarding
            } else {
                _authState.value = AuthState.LoggedIn
            }
        }
    }
}
