package com.mealplan.ui.screens.home

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
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val totalHealthyDays: Int = 0,
    val todaysMealPlan: DailyMealPlan? = null,
    val weeklyMealPlan: WeeklyMealPlan? = null,
    val completedMeals: Set<MealType> = emptySet(),
    val userProfile: UserProfile? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Load user profile
            val profile = userRepository.getCurrentUser()
            val progress = userRepository.getUserProgress()
            val userId = firebaseAuth.currentUser?.uid ?: ""

            // Load today's check-ins
            val todayCheckIns = mealPlanRepository.getCheckInsForDate(userId, LocalDate.now())
            val completedMeals = todayCheckIns.map { it.mealType }.toSet()

            _uiState.update {
                it.copy(
                    userName = profile?.displayName?.split(" ")?.firstOrNull() ?: "",
                    totalHealthyDays = progress?.totalHealthyDays ?: 0,
                    userProfile = profile,
                    completedMeals = completedMeals
                )
            }

            // Generate or load meal plan
            if (profile != null) {
                val mealPlan = mealPlanRepository.generateWeeklyMealPlan(
                    userId = userId,
                    userProfile = profile,
                    startDate = getWeekStartDate()
                )

                val today = LocalDate.now()
                val todaysPlan = mealPlan.days.find { it.date == today }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyMealPlan = mealPlan,
                        todaysMealPlan = todaysPlan
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun generateMealPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val profile = _uiState.value.userProfile
            val userId = firebaseAuth.currentUser?.uid ?: ""

            if (profile != null) {
                val mealPlan = mealPlanRepository.generateWeeklyMealPlan(
                    userId = userId,
                    userProfile = profile,
                    startDate = getWeekStartDate()
                )

                val today = LocalDate.now()
                val todaysPlan = mealPlan.days.find { it.date == today }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyMealPlan = mealPlan,
                        todaysMealPlan = todaysPlan
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun checkInMeal(mealType: MealType, recipeId: String) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch

            // Check if already checked in
            if (_uiState.value.completedMeals.contains(mealType)) {
                return@launch
            }

            mealPlanRepository.createCheckIn(
                userId = userId,
                date = LocalDate.now(),
                mealType = mealType,
                status = CheckInStatus.FOLLOWED_PLAN,
                recipeId = recipeId
            ).onSuccess {
                // Update UI
                _uiState.update {
                    it.copy(completedMeals = it.completedMeals + mealType)
                }

                // Update progress
                userRepository.incrementCheckIn()

                // Check if this is first check-in today
                val todayCheckIns = mealPlanRepository.getCheckInsForDate(userId, LocalDate.now())
                if (todayCheckIns.size == 1) {
                    userRepository.incrementHealthyDay()
                }
            }
        }
    }

    fun swapMeal(mealType: MealType) {
        viewModelScope.launch {
            val currentPlan = _uiState.value.todaysMealPlan ?: return@launch
            val profile = _uiState.value.userProfile ?: return@launch

            val currentRecipe = when (mealType) {
                MealType.BREAKFAST -> currentPlan.breakfast
                MealType.LUNCH -> currentPlan.lunch
                MealType.DINNER -> currentPlan.dinner
                MealType.SNACK -> currentPlan.snacks.firstOrNull()
            } ?: return@launch

            val alternative = mealPlanRepository.getEasierAlternative(currentRecipe, profile)

            if (alternative != null) {
                val updatedPlan = when (mealType) {
                    MealType.BREAKFAST -> currentPlan.copy(breakfast = alternative)
                    MealType.LUNCH -> currentPlan.copy(lunch = alternative)
                    MealType.DINNER -> currentPlan.copy(dinner = alternative)
                    MealType.SNACK -> currentPlan.copy(snacks = listOf(alternative))
                }
                _uiState.update { it.copy(todaysMealPlan = updatedPlan) }
            }
        }
    }

    private fun getWeekStartDate(): LocalDate {
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek.value
        return today.minusDays((dayOfWeek - 1).toLong())
    }
}
