package com.mealplan.ui.screens.weeklyplan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.DailyMealPlan
import com.mealplan.domain.model.MealType
import com.mealplan.domain.model.Recipe
import com.mealplan.ui.components.DifficultyIndicator
import com.mealplan.ui.components.FullScreenLoading
import com.mealplan.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyPlanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRecipe: (String) -> Unit,
    viewModel: WeeklyPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("This Week's Plan") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Day selector
                DaySelector(
                    days = uiState.weekDays,
                    selectedDay = uiState.selectedDay,
                    onDaySelected = viewModel::selectDay
                )

                // Meals for selected day
                val selectedPlan = uiState.mealPlan?.days?.find { it.date == uiState.selectedDay }

                if (selectedPlan != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        selectedPlan.breakfast?.let { recipe ->
                            item {
                                MealListItem(
                                    mealType = MealType.BREAKFAST,
                                    recipe = recipe,
                                    onClick = { onNavigateToRecipe(recipe.id) }
                                )
                            }
                        }

                        selectedPlan.lunch?.let { recipe ->
                            item {
                                MealListItem(
                                    mealType = MealType.LUNCH,
                                    recipe = recipe,
                                    onClick = { onNavigateToRecipe(recipe.id) }
                                )
                            }
                        }

                        selectedPlan.dinner?.let { recipe ->
                            item {
                                MealListItem(
                                    mealType = MealType.DINNER,
                                    recipe = recipe,
                                    onClick = { onNavigateToRecipe(recipe.id) }
                                )
                            }
                        }

                        selectedPlan.snacks.forEach { recipe ->
                            item {
                                MealListItem(
                                    mealType = MealType.SNACK,
                                    recipe = recipe,
                                    onClick = { onNavigateToRecipe(recipe.id) }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No meals planned for this day",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SoftGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DaySelector(
    days: List<LocalDate>,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(days) { day ->
            DayChip(
                date = day,
                isSelected = day == selectedDay,
                isToday = day == LocalDate.now(),
                onClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
private fun DayChip(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayNumber = date.dayOfMonth.toString()

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    isSelected -> SoftSageGreen
                    isToday -> SoftSageGreen.copy(alpha = 0.2f)
                    else -> SoftWhite
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) White else SoftGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dayNumber,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) White else WarmCharcoal
        )
    }
}

@Composable
private fun MealListItem(
    mealType: MealType,
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Meal type indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftSageGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getMealEmoji(mealType),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mealType.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = SoftGray
                )
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = WarmCharcoal
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = SoftGray
                    )
                    Text(
                        text = "${recipe.totalTimeMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                    DifficultyIndicator(difficulty = recipe.difficulty)
                }
            }
        }
    }
}

private fun getMealEmoji(mealType: MealType): String {
    return when (mealType) {
        MealType.BREAKFAST -> "\uD83C\uDF73"
        MealType.LUNCH -> "\uD83E\uDD57"
        MealType.DINNER -> "\uD83C\uDF5D"
        MealType.SNACK -> "\uD83C\uDF4E"
    }
}
