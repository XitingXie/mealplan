package com.mealplan.ui.screens.wellness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.domain.model.PostMealFeeling
import com.mealplan.domain.model.TrendRating
import com.mealplan.domain.model.WellnessCheckIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

data class WellnessUiState(
    val energyLevel: Int = 3,
    val digestionQuality: TrendRating? = null,
    val postMealFeeling: PostMealFeeling? = null,
    val sleepQuality: Int = 3,
    val overallMood: Int = 3,
    val receivedCompliment: Boolean? = null,
    val complimentNote: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class WellnessViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(WellnessUiState())
    val uiState: StateFlow<WellnessUiState> = _uiState.asStateFlow()

    fun setEnergyLevel(level: Int) {
        _uiState.update { it.copy(energyLevel = level) }
    }

    fun setDigestionQuality(quality: TrendRating) {
        _uiState.update { it.copy(digestionQuality = quality) }
    }

    fun setPostMealFeeling(feeling: PostMealFeeling) {
        _uiState.update { it.copy(postMealFeeling = feeling) }
    }

    fun setSleepQuality(quality: Int) {
        _uiState.update { it.copy(sleepQuality = quality) }
    }

    fun setOverallMood(mood: Int) {
        _uiState.update { it.copy(overallMood = mood) }
    }

    fun setReceivedCompliment(received: Boolean) {
        _uiState.update { it.copy(receivedCompliment = received) }
    }

    fun setComplimentNote(note: String) {
        _uiState.update { it.copy(complimentNote = note) }
    }

    fun saveCheckIn() {
        val state = _uiState.value
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Calculate week number since app start (approximate)
            val weekNumber = ChronoUnit.WEEKS.between(
                LocalDate.now().minusMonths(1), // Approximate start
                LocalDate.now()
            ).toInt() + 1

            val checkIn = WellnessCheckIn(
                id = UUID.randomUUID().toString(),
                userId = userId,
                weekNumber = weekNumber,
                date = LocalDate.now(),
                energyLevel = state.energyLevel,
                digestionQuality = state.digestionQuality ?: TrendRating.SAME,
                postMealFeeling = state.postMealFeeling ?: PostMealFeeling.LIGHT_SATISFIED,
                sleepQuality = state.sleepQuality,
                receivedCompliments = state.receivedCompliment ?: false,
                complimentNote = if (state.receivedCompliment == true) state.complimentNote else null,
                overallMood = state.overallMood
            )

            mealPlanRepository.createWellnessCheckIn(checkIn)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }
}
