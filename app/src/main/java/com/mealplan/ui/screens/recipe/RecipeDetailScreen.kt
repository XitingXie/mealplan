package com.mealplan.ui.screens.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.Ingredient
import com.mealplan.domain.model.NutritionInfo
import com.mealplan.domain.model.Recipe
import com.mealplan.ui.components.DifficultyIndicator
import com.mealplan.ui.components.FullScreenLoading
import com.mealplan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onNavigateBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmCream
                )
            )
        },
        containerColor = WarmCream
    ) { padding ->
        if (uiState.isLoading) {
            FullScreenLoading()
        } else {
            uiState.recipe?.let { recipe ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    item {
                        RecipeHeader(recipe = recipe)
                    }

                    // Quick stats
                    item {
                        QuickStats(recipe = recipe)
                    }

                    // Ingredients
                    item {
                        IngredientsSection(
                            ingredients = recipe.ingredients,
                            substitutions = recipe.substitutions
                        )
                    }

                    // Instructions
                    item {
                        InstructionsSection(instructions = recipe.instructions)
                    }

                    // Nutrition
                    item {
                        NutritionSection(nutrition = recipe.nutrition)
                    }

                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeHeader(recipe: Recipe) {
    Column {
        // Recipe image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SoftSageGreen.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83C\uDF7D\uFE0F",
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = recipe.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = recipe.description,
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )
    }
}

@Composable
private fun QuickStats(recipe: Recipe) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            icon = Icons.Outlined.Timer,
            label = "Prep",
            value = "${recipe.prepTimeMinutes} min"
        )
        StatItem(
            icon = Icons.Outlined.Whatshot,
            label = "Cook",
            value = "${recipe.cookTimeMinutes} min"
        )
        StatItem(
            icon = Icons.Outlined.Restaurant,
            label = "Servings",
            value = recipe.servings.toString()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Difficulty",
                style = MaterialTheme.typography.labelSmall,
                color = SoftGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            DifficultyIndicator(difficulty = recipe.difficulty)
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = SoftSageGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = WarmCharcoal
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SoftGray
        )
    }
}

@Composable
private fun IngredientsSection(
    ingredients: List<Ingredient>,
    substitutions: Map<String, String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(12.dp))

            ingredients.forEach { ingredient ->
                IngredientItem(
                    ingredient = ingredient,
                    substitution = substitutions[ingredient.name]
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun IngredientItem(
    ingredient: Ingredient,
    substitution: String?
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(SoftSageGreen)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = ingredient.displayString,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmCharcoal
            )
        }
        if (substitution != null) {
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = "Substitute",
                    modifier = Modifier.size(14.dp),
                    tint = SoftCoral
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sub: $substitution",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftCoral
                )
            }
        }
    }
}

@Composable
private fun InstructionsSection(instructions: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(12.dp))

            instructions.forEachIndexed { index, instruction ->
                InstructionStep(
                    stepNumber = index + 1,
                    instruction = instruction
                )
                if (index < instructions.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun InstructionStep(
    stepNumber: Int,
    instruction: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(SoftSageGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmCharcoal,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NutritionSection(nutrition: NutritionInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nutrition Facts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Text(
                text = "Per serving",
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionItem(label = "Calories", value = "${nutrition.calories}")
                NutritionItem(label = "Protein", value = "${nutrition.proteinGrams.toInt()}g")
                NutritionItem(label = "Carbs", value = "${nutrition.carbsGrams.toInt()}g")
                NutritionItem(label = "Fat", value = "${nutrition.fatGrams.toInt()}g")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionItem(label = "Fiber", value = "${nutrition.fiberGrams.toInt()}g")
                NutritionItem(label = "Sodium", value = "${nutrition.sodiumMg.toInt()}mg")
            }
        }
    }
}

@Composable
private fun NutritionItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = WarmCharcoal
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SoftGray
        )
    }
}
