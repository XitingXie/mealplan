package com.mealplan.ui.screens.scanner

import android.net.Uri
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

data class ScannerUiState(
    val selectedIngredients: Set<String> = emptySet(),
    val suggestedRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val capturedPhotoUri: Uri? = null
)

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun toggleIngredient(ingredient: String) {
        _uiState.update { state ->
            val updated = state.selectedIngredients.toMutableSet()
            if (updated.contains(ingredient)) {
                updated.remove(ingredient)
            } else {
                updated.add(ingredient)
            }
            state.copy(selectedIngredients = updated)
        }
        searchRecipes()
    }

    private fun searchRecipes() {
        viewModelScope.launch {
            val selected = _uiState.value.selectedIngredients
            if (selected.isEmpty()) {
                _uiState.update { it.copy(suggestedRecipes = emptyList()) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            // Get all recipes and filter by ingredients
            val allRecipes = mealPlanRepository.getAllRecipes()

            // Score recipes by how many selected ingredients they contain
            val scoredRecipes = allRecipes.map { recipe ->
                val ingredientNames = recipe.ingredients.map { it.name.lowercase() }
                val matches = selected.count { selectedIngredient ->
                    ingredientNames.any { it.contains(selectedIngredient.lowercase()) }
                }
                recipe to matches
            }
                .filter { it.second > 0 }
                .sortedByDescending { it.second }
                .take(10)
                .map { it.first }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    suggestedRecipes = scoredRecipes
                )
            }
        }
    }

    fun onPhotoTaken(uri: Uri) {
        _uiState.update { it.copy(capturedPhotoUri = uri) }
    }

    fun clearPhoto() {
        _uiState.update { it.copy(capturedPhotoUri = null) }
    }
}
