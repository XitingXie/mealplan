package com.mealplan.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplan.data.repository.MealPlanRepository
import com.mealplan.domain.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val recipe = mealPlanRepository.getRecipeById(recipeId)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    recipe = recipe,
                    errorMessage = if (recipe == null) "Recipe not found" else null
                )
            }
        }
    }
}
