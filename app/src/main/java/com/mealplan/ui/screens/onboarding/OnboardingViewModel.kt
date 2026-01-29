package com.mealplan.ui.screens.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.data.repository.UserRepository
import com.mealplan.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

data class OnboardingUiState(
    val currentStep: Int = 0,
    val healthGoal: HealthGoal? = null,
    val dietaryRestrictions: Set<DietaryRestriction> = emptySet(),
    val cookingSkill: CookingSkill? = null,
    val mealPrepDays: Set<DayOfWeek> = emptySet(),
    val householdSize: Int = 1,
    val budgetPreference: BudgetPreference? = null,
    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextStep() {
        _uiState.update { it.copy(currentStep = minOf(it.currentStep + 1, 7)) }
    }

    fun previousStep() {
        _uiState.update { it.copy(currentStep = maxOf(it.currentStep - 1, 0)) }
    }

    fun canProceed(): Boolean {
        val state = _uiState.value
        return when (state.currentStep) {
            0 -> true // Welcome screen
            1 -> state.healthGoal != null
            2 -> true // Dietary restrictions can be empty
            3 -> state.cookingSkill != null
            4 -> true // Meal prep days can be empty
            5 -> state.householdSize > 0
            6 -> state.budgetPreference != null
            7 -> true // Completion screen
            else -> false
        }
    }

    fun setHealthGoal(goal: HealthGoal) {
        _uiState.update { it.copy(healthGoal = goal) }
    }

    fun toggleDietaryRestriction(restriction: DietaryRestriction) {
        _uiState.update { state ->
            val current = state.dietaryRestrictions.toMutableSet()
            if (restriction == DietaryRestriction.NONE) {
                // If selecting "None", clear all others
                state.copy(dietaryRestrictions = setOf(DietaryRestriction.NONE))
            } else {
                // Remove "None" if selecting something else
                current.remove(DietaryRestriction.NONE)
                if (current.contains(restriction)) {
                    current.remove(restriction)
                } else {
                    current.add(restriction)
                }
                state.copy(dietaryRestrictions = current)
            }
        }
    }

    fun setCookingSkill(skill: CookingSkill) {
        _uiState.update { it.copy(cookingSkill = skill) }
    }

    fun toggleMealPrepDay(day: DayOfWeek) {
        _uiState.update { state ->
            val current = state.mealPrepDays.toMutableSet()
            if (current.contains(day)) {
                current.remove(day)
            } else {
                current.add(day)
            }
            state.copy(mealPrepDays = current)
        }
    }

    fun setHouseholdSize(size: Int) {
        _uiState.update { it.copy(householdSize = size) }
    }

    fun setBudgetPreference(budget: BudgetPreference) {
        _uiState.update { it.copy(budgetPreference = budget) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            Log.d("OnboardingViewModel", "completeOnboarding: start")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            val user = firebaseAuth.currentUser

            if (user == null) {
                Log.w("OnboardingViewModel", "completeOnboarding: no current user")
                _uiState.update { it.copy(isLoading = false, errorMessage = "Not signed in") }
                return@launch
            }

            val profile = UserProfile(
                uid = user.uid,
                email = user.email ?: "",
                displayName = user.displayName ?: "",
                healthGoal = state.healthGoal ?: HealthGoal.GENERAL_WELLNESS,
                dietaryRestrictions = state.dietaryRestrictions.toList(),
                cookingSkill = state.cookingSkill ?: CookingSkill.BEGINNER,
                mealPrepDays = state.mealPrepDays.toList(),
                householdSize = state.householdSize,
                budgetPreference = state.budgetPreference ?: BudgetPreference.MODERATE,
                onboardingCompleted = true
            )

            // Save user profile
            Log.d("OnboardingViewModel", "completeOnboarding: saving profile")
            userRepository.completeOnboarding(profile)
                .onSuccess {
                    Log.d("OnboardingViewModel", "completeOnboarding: profile saved")
                    // Initialize mock recipes
                    try {
                        Log.d("OnboardingViewModel", "completeOnboarding: init mock recipes")
                        mealPlanRepository.initializeMockRecipes()
                        Log.d("OnboardingViewModel", "completeOnboarding: mock recipes done")
                    } catch (e: Exception) {
                        Log.e("OnboardingViewModel", "completeOnboarding: mock recipes failed", e)
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = e.message ?: "Init recipes failed")
                        }
                        return@onSuccess
                    }

                    // Initialize user progress
                    try {
                        Log.d("OnboardingViewModel", "completeOnboarding: init user progress")
                        userRepository.initializeUserProgress()
                        Log.d("OnboardingViewModel", "completeOnboarding: user progress done")
                    } catch (e: Exception) {
                        Log.e("OnboardingViewModel", "completeOnboarding: user progress failed", e)
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = e.message ?: "Init progress failed")
                        }
                        return@onSuccess
                    }

                    _uiState.update { it.copy(isLoading = false, isComplete = true) }
                }
                .onFailure { error ->
                    Log.e("OnboardingViewModel", "completeOnboarding: save failed", error)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }
}
