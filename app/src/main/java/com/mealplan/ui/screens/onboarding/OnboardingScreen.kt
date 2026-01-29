package com.mealplan.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.*
import com.mealplan.ui.components.LoadingButton
import com.mealplan.ui.theme.*
import java.time.DayOfWeek

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onOnboardingComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WarmCream, SoftLavenderTint)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = { (uiState.currentStep + 1) / 8f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = SoftSageGreen,
                trackColor = LightGray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content area
            Box(modifier = Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = uiState.currentStep,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "onboarding_content",
                    modifier = Modifier.fillMaxSize()
                ) { step ->
                    when (step) {
                        0 -> WelcomeStep()
                        1 -> HealthGoalStep(
                            selectedGoal = uiState.healthGoal,
                            onGoalSelected = viewModel::setHealthGoal
                        )
                        2 -> DietaryRestrictionsStep(
                            selectedRestrictions = uiState.dietaryRestrictions,
                            onRestrictionToggled = viewModel::toggleDietaryRestriction
                        )
                        3 -> CookingSkillStep(
                            selectedSkill = uiState.cookingSkill,
                            onSkillSelected = viewModel::setCookingSkill
                        )
                        4 -> MealPrepDaysStep(
                            selectedDays = uiState.mealPrepDays,
                            onDayToggled = viewModel::toggleMealPrepDay
                        )
                        5 -> HouseholdSizeStep(
                            selectedSize = uiState.householdSize,
                            onSizeSelected = viewModel::setHouseholdSize
                        )
                        6 -> BudgetStep(
                            selectedBudget = uiState.budgetPreference,
                            onBudgetSelected = viewModel::setBudgetPreference
                        )
                        7 -> CompletionStep(uiState = uiState)
                    }
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.currentStep > 0) {
                    OutlinedButton(
                        onClick = viewModel::previousStep,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Back")
                    }
                }

                if (uiState.currentStep < 7) {
                    Button(
                        onClick = viewModel::nextStep,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(28.dp),
                        enabled = viewModel.canProceed()
                    ) {
                        Text(if (uiState.currentStep == 0) "Get Started" else "Continue")
                    }
                } else {
                    LoadingButton(
                        text = "Generate My Meal Plan",
                        isLoading = uiState.isLoading,
                        onClick = viewModel::completeOnboarding,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "\uD83C\uDF7D\uFE0F",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Let's personalize\nyour meal plan",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Answer a few questions so we can create the perfect meal plan for you. This takes less than 2 minutes.",
            style = MaterialTheme.typography.bodyLarge,
            color = SoftGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HealthGoalStep(
    selectedGoal: HealthGoal?,
    onGoalSelected: (HealthGoal) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "What's your health goal?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose your primary focus for eating better",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(HealthGoal.entries) { goal ->
                SelectableCard(
                    title = "${goal.emoji} ${goal.displayName}",
                    description = goal.description,
                    isSelected = selectedGoal == goal,
                    onClick = { onGoalSelected(goal) }
                )
            }
        }
    }
}

@Composable
private fun DietaryRestrictionsStep(
    selectedRestrictions: Set<DietaryRestriction>,
    onRestrictionToggled: (DietaryRestriction) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Any dietary restrictions?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Select all that apply (or none)",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(DietaryRestriction.entries) { restriction ->
                MultiSelectCard(
                    title = restriction.displayName,
                    isSelected = selectedRestrictions.contains(restriction),
                    onClick = { onRestrictionToggled(restriction) }
                )
            }
        }
    }
}

@Composable
private fun CookingSkillStep(
    selectedSkill: CookingSkill?,
    onSkillSelected: (CookingSkill) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What's your cooking level?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll match recipes to your comfort level",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CookingSkill.entries.forEach { skill ->
                SelectableCard(
                    title = skill.displayName,
                    description = skill.description,
                    isSelected = selectedSkill == skill,
                    onClick = { onSkillSelected(skill) }
                )
            }
        }
    }
}

@Composable
private fun MealPrepDaysStep(
    selectedDays: Set<DayOfWeek>,
    onDayToggled: (DayOfWeek) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Which days do you cook?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll plan meals around your cooking days",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DayOfWeek.entries) { day ->
                DayChip(
                    day = day,
                    isSelected = selectedDays.contains(day),
                    onClick = { onDayToggled(day) }
                )
            }
        }
    }
}

@Composable
private fun HouseholdSizeStep(
    selectedSize: Int,
    onSizeSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "How many people are you cooking for?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll adjust portion sizes accordingly",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(1, 2, 3, 4).forEach { size ->
                SizeButton(
                    size = size,
                    isSelected = selectedSize == size,
                    onClick = { onSizeSelected(size) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BudgetStep(
    selectedBudget: BudgetPreference?,
    onBudgetSelected: (BudgetPreference) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What's your budget preference?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll suggest ingredients that fit your budget",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BudgetPreference.entries.forEach { budget ->
                SelectableCard(
                    title = budget.displayName,
                    description = budget.description,
                    isSelected = selectedBudget == budget,
                    onClick = { onBudgetSelected(budget) }
                )
            }
        }
    }
}

@Composable
private fun CompletionStep(uiState: OnboardingUiState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83C\uDF89",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You're all set!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WarmCharcoal
        )

        Spacer(modifier = Modifier.height(24.dp))

        SummaryCard(uiState = uiState)
    }
}

@Composable
private fun SummaryCard(uiState: OnboardingUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Your preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            SummaryRow("Goal", "${uiState.healthGoal?.emoji ?: ""} ${uiState.healthGoal?.displayName ?: ""}")
            SummaryRow("Skill", uiState.cookingSkill?.displayName ?: "")
            SummaryRow("Household", "${uiState.householdSize} ${if (uiState.householdSize == 1) "person" else "people"}")
            SummaryRow("Budget", uiState.budgetPreference?.displayName ?: "")

            if (uiState.dietaryRestrictions.isNotEmpty() &&
                !uiState.dietaryRestrictions.contains(DietaryRestriction.NONE)) {
                SummaryRow("Diet", uiState.dietaryRestrictions.joinToString(", ") { it.displayName })
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = SoftGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = WarmCharcoal
        )
    }
}

@Composable
private fun SelectableCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(2.dp, SoftSageGreen, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SoftSageGreen.copy(alpha = 0.1f) else SoftWhite
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = WarmCharcoal
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGray
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(SoftSageGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiSelectCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(2.dp, SoftSageGreen, RoundedCornerShape(12.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SoftSageGreen.copy(alpha = 0.1f) else SoftWhite
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = WarmCharcoal
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SoftSageGreen,
                    uncheckedColor = LightGray
                )
            )
        }
    }
}

@Composable
private fun DayChip(
    day: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayName = day.name.take(3).lowercase().replaceFirstChar { it.uppercase() }

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(if (isSelected) SoftSageGreen else SoftWhite)
            .clickable(onClick = onClick)
            .then(
                if (!isSelected) Modifier.border(1.dp, LightGray, CircleShape) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) White else SoftGray
        )
    }
}

@Composable
private fun SizeButton(
    size: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (size == 4) "4+" else size.toString()

    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) SoftSageGreen else SoftWhite,
            contentColor = if (isSelected) White else WarmCharcoal
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 0.dp else 2.dp
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
