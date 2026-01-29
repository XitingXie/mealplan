package com.mealplan.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplan.data.repository.AuthRepository
import com.mealplan.data.repository.UserRepository
import com.mealplan.domain.model.UserProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userName: String = "",
    val email: String = "",
    val progress: UserProgress? = null,
    val isLoading: Boolean = true,
    val isSignedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = authRepository.currentUser
            val profile = userRepository.getCurrentUser()
            val progress = userRepository.getUserProgress()

            _uiState.update {
                it.copy(
                    userName = profile?.displayName ?: user?.displayName ?: "",
                    email = profile?.email ?: user?.email ?: "",
                    progress = progress,
                    isLoading = false
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(isSignedOut = true) }
        }
    }
}
