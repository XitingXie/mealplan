package com.mealplan.ui.screens.wellness

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.PostMealFeeling
import com.mealplan.domain.model.TrendRating
import com.mealplan.ui.components.LoadingButton
import com.mealplan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellnessCheckInScreen(
    onNavigateBack: () -> Unit,
    viewModel: WellnessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Check-in") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "How are you feeling this week?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = WarmCharcoal
                )
                Text(
                    text = "Track your wellness to see how healthy eating helps you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGray
                )
            }

            // Energy level
            item {
                RatingQuestion(
                    title = "Energy Level",
                    emoji = "\u26A1",
                    description = "How's your energy been?",
                    value = uiState.energyLevel,
                    onValueChange = viewModel::setEnergyLevel
                )
            }

            // Digestion
            item {
                TrendQuestion(
                    title = "Digestion",
                    emoji = "\uD83E\uDDB4",
                    description = "How's your digestion/regularity?",
                    value = uiState.digestionQuality,
                    onValueChange = viewModel::setDigestionQuality
                )
            }

            // Post-meal feeling
            item {
                FeelingQuestion(
                    title = "After Meals",
                    emoji = "\uD83C\uDF7D\uFE0F",
                    description = "How do you feel after eating?",
                    value = uiState.postMealFeeling,
                    onValueChange = viewModel::setPostMealFeeling
                )
            }

            // Sleep quality
            item {
                RatingQuestion(
                    title = "Sleep Quality",
                    emoji = "\uD83D\uDE34",
                    description = "How well are you sleeping?",
                    value = uiState.sleepQuality,
                    onValueChange = viewModel::setSleepQuality
                )
            }

            // Overall mood
            item {
                RatingQuestion(
                    title = "Overall Mood",
                    emoji = "\uD83D\uDE0A",
                    description = "How's your mood been?",
                    value = uiState.overallMood,
                    onValueChange = viewModel::setOverallMood
                )
            }

            // Compliments
            item {
                ComplimentQuestion(
                    receivedCompliment = uiState.receivedCompliment,
                    onComplimentChange = viewModel::setReceivedCompliment,
                    complimentNote = uiState.complimentNote,
                    onNoteChange = viewModel::setComplimentNote
                )
            }

            // Submit button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                LoadingButton(
                    text = "Save Check-in",
                    isLoading = uiState.isLoading,
                    onClick = viewModel::saveCheckIn,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun RatingQuestion(
    title: String,
    emoji: String,
    description: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmCharcoal
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { rating ->
                    RatingCircle(
                        number = rating,
                        isSelected = value == rating,
                        onClick = { onValueChange(rating) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Low",
                    style = MaterialTheme.typography.labelSmall,
                    color = SoftGray
                )
                Text(
                    text = "High",
                    style = MaterialTheme.typography.labelSmall,
                    color = SoftGray
                )
            }
        }
    }
}

@Composable
private fun RatingCircle(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (isSelected) SoftSageGreen else LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) White else WarmCharcoal
        )
    }
}

@Composable
private fun TrendQuestion(
    title: String,
    emoji: String,
    description: String,
    value: TrendRating?,
    onValueChange: (TrendRating) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmCharcoal
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrendRating.entries.forEach { rating ->
                    TrendChip(
                        rating = rating,
                        isSelected = value == rating,
                        onClick = { onValueChange(rating) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendChip(
    rating: TrendRating,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SoftSageGreen else LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = rating.emoji, style = MaterialTheme.typography.titleMedium)
            Text(
                text = rating.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) White else WarmCharcoal
            )
        }
    }
}

@Composable
private fun FeelingQuestion(
    title: String,
    emoji: String,
    description: String,
    value: PostMealFeeling?,
    onValueChange: (PostMealFeeling) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmCharcoal
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PostMealFeeling.entries.forEach { feeling ->
                    FeelingOption(
                        feeling = feeling,
                        isSelected = value == feeling,
                        onClick = { onValueChange(feeling) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeelingOption(
    feeling: PostMealFeeling,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SoftSageGreen.copy(alpha = 0.2f) else Transparent)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SoftSageGreen
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = feeling.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmCharcoal
            )
            Text(
                text = feeling.description,
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray
            )
        }
    }
}

@Composable
private fun ComplimentQuestion(
    receivedCompliment: Boolean?,
    onComplimentChange: (Boolean) -> Unit,
    complimentNote: String,
    onNoteChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "\u2728", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Any Compliments?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmCharcoal
                    )
                    Text(
                        text = "Did anyone notice you looking healthier?",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(true to "Yes!", false to "Not yet").forEach { (value, label) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (receivedCompliment == value) SoftSageGreen
                                else LightGray.copy(alpha = 0.3f)
                            )
                            .clickable { onComplimentChange(value) }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (receivedCompliment == value) White else WarmCharcoal
                        )
                    }
                }
            }

            if (receivedCompliment == true) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = complimentNote,
                    onValueChange = onNoteChange,
                    label = { Text("What did they say? (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
