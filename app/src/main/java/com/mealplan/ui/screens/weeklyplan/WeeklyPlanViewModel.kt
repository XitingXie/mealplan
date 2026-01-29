package com.mealplan.ui.screens.weeklyplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.data.repository.UserRepository
import com.mealplan.domain.model.WeeklyMealPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class WeeklyPlanUiState(
    val isLoading: Boolean = true,
    val mealPlan: WeeklyMealPlan? = null,
    val weekDays: List<LocalDate> = emptyList(),
    val selectedDay: LocalDate? = null
)

@HiltViewModel
class WeeklyPlanViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyPlanUiState())
    val uiState: StateFlow<WeeklyPlanUiState> = _uiState.asStateFlow()

    init {
        loadWeeklyPlan()
    }

    private fun loadWeeklyPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val today = LocalDate.now()
            val weekStart = getWeekStartDate()
            val weekDays = (0 until 7).map { weekStart.plusDays(it.toLong()) }

            val profile = userRepository.getCurrentUser()
            val userId = firebaseAuth.currentUser?.uid ?: ""

            if (profile != null) {
                val mealPlan = mealPlanRepository.generateWeeklyMealPlan(
                    userId = userId,
                    userProfile = profile,
                    startDate = weekStart
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mealPlan = mealPlan,
                        weekDays = weekDays,
                        selectedDay = today
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weekDays = weekDays,
                        selectedDay = today
                    )
                }
            }
        }
    }

    fun selectDay(day: LocalDate) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    private fun getWeekStartDate(): LocalDate {
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek.value
        return today.minusDays((dayOfWeek - 1).toLong())
    }
}
