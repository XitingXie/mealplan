package com.mealplan.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.MealType
import com.mealplan.domain.model.Recipe
import com.mealplan.ui.components.FullScreenLoading
import com.mealplan.ui.components.MealCard
import com.mealplan.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRecipe: (String) -> Unit,
    onNavigateToWeeklyPlan: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToQuickLog: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        FullScreenLoading(message = "Loading your meal plan...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = WarmCharcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmCream
                )
            )
        },
        bottomBar = {
            QuickActionsBar(
                onScannerClick = onNavigateToScanner,
                onQuickLogClick = onNavigateToQuickLog,
                onWeeklyPlanClick = onNavigateToWeeklyPlan
            )
        },
        containerColor = WarmCream
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting header
            item {
                GreetingHeader(
                    userName = uiState.userName,
                    totalHealthyDays = uiState.totalHealthyDays
                )
            }

            // Today's date
            item {
                TodayHeader()
            }

            // Today's meals
            val todaysPlan = uiState.todaysMealPlan
            if (todaysPlan != null) {
                todaysPlan.breakfast?.let { recipe ->
                    item {
                        MealCard(
                            recipe = recipe,
                            mealType = MealType.BREAKFAST,
                            isCompleted = uiState.completedMeals.contains(MealType.BREAKFAST),
                            onCardClick = { onNavigateToRecipe(recipe.id) },
                            onCheckInClick = { viewModel.checkInMeal(MealType.BREAKFAST, recipe.id) },
                            onPhotoClick = onNavigateToQuickLog,
                            onSwapClick = { viewModel.swapMeal(MealType.BREAKFAST) }
                        )
                    }
                }

                todaysPlan.lunch?.let { recipe ->
                    item {
                        MealCard(
                            recipe = recipe,
                            mealType = MealType.LUNCH,
                            isCompleted = uiState.completedMeals.contains(MealType.LUNCH),
                            onCardClick = { onNavigateToRecipe(recipe.id) },
                            onCheckInClick = { viewModel.checkInMeal(MealType.LUNCH, recipe.id) },
                            onPhotoClick = onNavigateToQuickLog,
                            onSwapClick = { viewModel.swapMeal(MealType.LUNCH) }
                        )
                    }
                }

                todaysPlan.dinner?.let { recipe ->
                    item {
                        MealCard(
                            recipe = recipe,
                            mealType = MealType.DINNER,
                            isCompleted = uiState.completedMeals.contains(MealType.DINNER),
                            onCardClick = { onNavigateToRecipe(recipe.id) },
                            onCheckInClick = { viewModel.checkInMeal(MealType.DINNER, recipe.id) },
                            onPhotoClick = onNavigateToQuickLog,
                            onSwapClick = { viewModel.swapMeal(MealType.DINNER) }
                        )
                    }
                }

                todaysPlan.snacks.forEachIndexed { index, recipe ->
                    item {
                        MealCard(
                            recipe = recipe,
                            mealType = MealType.SNACK,
                            isCompleted = false, // Track snacks separately if needed
                            onCardClick = { onNavigateToRecipe(recipe.id) },
                            onCheckInClick = { viewModel.checkInMeal(MealType.SNACK, recipe.id) },
                            onPhotoClick = onNavigateToQuickLog,
                            onSwapClick = { viewModel.swapMeal(MealType.SNACK) }
                        )
                    }
                }
            } else {
                item {
                    EmptyMealPlanCard(onGeneratePlan = viewModel::generateMealPlan)
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun GreetingHeader(
    userName: String,
    totalHealthyDays: Int
) {
    Column {
        Text(
            text = getGreeting(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        if (userName.isNotEmpty()) {
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                color = SoftSageGreen
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (totalHealthyDays > 0) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MintGreen.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "\uD83C\uDF1F", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "You've had $totalHealthyDays healthy ${if (totalHealthyDays == 1) "day" else "days"} so far!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmCharcoal
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayHeader() {
    val today = LocalDate.now()
    val dayName = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dateStr = today.format(DateTimeFormatter.ofPattern("MMMM d"))

    Text(
        text = "$dayName, $dateStr",
        style = MaterialTheme.typography.titleMedium,
        color = SoftGray
    )
}

@Composable
private fun QuickActionsBar(
    onScannerClick: () -> Unit,
    onQuickLogClick: () -> Unit,
    onWeeklyPlanClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = SoftWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem(
                icon = Icons.Outlined.Kitchen,
                label = "What's in my fridge?",
                onClick = onScannerClick
            )
            QuickActionItem(
                icon = Icons.Outlined.CameraAlt,
                label = "Quick log",
                onClick = onQuickLogClick
            )
            QuickActionItem(
                icon = Icons.Outlined.CalendarMonth,
                label = "This week",
                onClick = onWeeklyPlanClick
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = SoftSageGreen.copy(alpha = 0.15f),
                contentColor = SoftSageGreen
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SoftGray
        )
    }
}

@Composable
private fun EmptyMealPlanCard(
    onGeneratePlan: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftLavenderTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83C\uDF7D\uFE0F",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No meal plan yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Let's create your personalized meal plan",
                style = MaterialTheme.typography.bodyMedium,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGeneratePlan,
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Generate Meal Plan")
            }
        }
    }
}

private fun getGreeting(): String {
    val hour = java.time.LocalTime.now().hour
    return when {
        hour < 12 -> "Good morning!"
        hour < 17 -> "Good afternoon!"
        else -> "Good evening!"
    }
}
