package com.mealplan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mealplan.domain.model.CookingSkill
import com.mealplan.domain.model.MealType
import com.mealplan.domain.model.Recipe
import com.mealplan.ui.theme.*

@Composable
fun MealCard(
    recipe: Recipe,
    mealType: MealType,
    isCompleted: Boolean = false,
    onCardClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onSwapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MintGreen.copy(alpha = 0.2f) else SoftLavenderTint
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Meal type header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                SoftSageGreen.copy(alpha = 0.3f),
                                WarmPeach.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = mealType.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmCharcoal
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Recipe image placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftSageGreen.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (recipe.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = recipe.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = recipe.name.take(1),
                            style = MaterialTheme.typography.headlineMedium,
                            color = SoftSageGreen
                        )
                    }
                }

                // Recipe info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = WarmCharcoal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prep time
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = "Prep time",
                                modifier = Modifier.size(14.dp),
                                tint = SoftGray
                            )
                            Text(
                                text = "${recipe.totalTimeMinutes} min",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftGray
                            )
                        }

                        // Difficulty dots
                        DifficultyIndicator(difficulty = recipe.difficulty)
                    }
                }
            }

            // Quick action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Check-in button
                QuickActionButton(
                    icon = Icons.Outlined.Check,
                    label = if (isCompleted) "Done" else "Mark Done",
                    isActive = isCompleted,
                    onClick = onCheckInClick,
                    modifier = Modifier.weight(1f)
                )

                // Photo button
                QuickActionButton(
                    icon = Icons.Outlined.CameraAlt,
                    label = "Photo",
                    onClick = onPhotoClick,
                    modifier = Modifier.weight(1f)
                )

                // Swap button
                QuickActionButton(
                    icon = Icons.Outlined.SwapHoriz,
                    label = "Swap",
                    onClick = onSwapClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DifficultyIndicator(
    difficulty: CookingSkill,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        val dotCount = when (difficulty) {
            CookingSkill.BEGINNER -> 1
            CookingSkill.INTERMEDIATE -> 2
            CookingSkill.ADVANCED -> 3
        }

        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index < dotCount) SoftCoral else LightGray.copy(alpha = 0.5f)
                    )
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isActive) MintGreen.copy(alpha = 0.3f) else Transparent,
            contentColor = if (isActive) SoftSageGreen else SoftGray
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
