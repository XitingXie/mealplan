package com.mealplan.ui.screens.quicklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.data.repository.UserRepository
import com.mealplan.domain.model.CheckInStatus
import com.mealplan.domain.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class QuickLogUiState(
    val selectedMealType: MealType? = null,
    val selectedQuickPick: String? = null,
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class QuickLogViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickLogUiState())
    val uiState: StateFlow<QuickLogUiState> = _uiState.asStateFlow()

    init {
        // Auto-suggest meal type based on time
        val suggestedType = getSuggestedMealType()
        _uiState.update { it.copy(selectedMealType = suggestedType) }
    }

    fun selectMealType(type: MealType) {
        _uiState.update { it.copy(selectedMealType = type) }
    }

    fun selectQuickPick(option: String) {
        _uiState.update { it.copy(selectedQuickPick = option) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun logMeal() {
        val state = _uiState.value
        val mealType = state.selectedMealType ?: return
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val notes = buildString {
                state.selectedQuickPick?.let { append(it) }
                if (state.notes.isNotBlank()) {
                    if (isNotEmpty()) append(" - ")
                    append(state.notes)
                }
            }

            mealPlanRepository.createCheckIn(
                userId = userId,
                date = LocalDate.now(),
                mealType = mealType,
                status = CheckInStatus.UPLOADED_PHOTO, // Using this for quick logs
                notes = notes
            ).onSuccess {
                userRepository.incrementCheckIn()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    private fun getSuggestedMealType(): MealType {
        val hour = LocalTime.now().hour
        return when {
            hour < 11 -> MealType.BREAKFAST
            hour < 15 -> MealType.LUNCH
            else -> MealType.DINNER
        }
    }
}
