package com.mealplan.ui.screens.wellness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.mealplan.domain.model.BenefitsTimeline
import com.mealplan.domain.model.ExpectedBenefit
import com.mealplan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenefitsTimelineScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Benefits Timeline") },
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
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "What to expect on your journey",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = WarmCharcoal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Healthy eating brings many benefits over time. Here's what most people experience:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            itemsIndexed(BenefitsTimeline.benefits) { index, benefit ->
                BenefitTimelineItem(
                    benefit = benefit,
                    isFirst = index == 0,
                    isLast = index == BenefitsTimeline.benefits.lastIndex
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                EncouragementCard()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun BenefitTimelineItem(
    benefit: ExpectedBenefit,
    isFirst: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Timeline connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(48.dp)
        ) {
            // Top line
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(16.dp)
                        .background(SoftSageGreen.copy(alpha = 0.3f))
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(SoftSageGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getTimelineEmoji(benefit.weekRange.first),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            // Bottom line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .background(SoftSageGreen.copy(alpha = 0.3f))
                )
            }
        }

        // Content card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftWhite)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Time range
                Text(
                    text = getTimeRangeText(benefit.weekRange),
                    style = MaterialTheme.typography.labelMedium,
                    color = SoftSageGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = benefit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCharcoal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = benefit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGray
                )
            }
        }
    }
}

@Composable
private fun EncouragementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83C\uDF1F",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Remember",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Everyone's journey is different. These are general timelines - you might experience benefits sooner or later. The important thing is that you're taking care of yourself.",
                style = MaterialTheme.typography.bodyMedium,
                color = SoftGray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Every healthy meal is a step forward.",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = SoftSageGreen
            )
        }
    }
}

private fun getTimeRangeText(range: IntRange): String {
    return when {
        range.first == range.last -> "Week ${range.first}"
        range.last <= 4 -> "Week ${range.first}-${range.last}"
        range.last <= 12 -> "Month ${range.first / 4 + 1}-${range.last / 4}"
        else -> "Month ${range.first / 4}+"
    }
}

private fun getTimelineEmoji(weekStart: Int): String {
    return when {
        weekStart <= 2 -> "\uD83C\uDF31"  // Seedling
        weekStart <= 4 -> "\uD83C\uDF3F"  // Herb
        weekStart <= 8 -> "\uD83C\uDF3B"  // Sunflower
        else -> "\uD83C\uDF33"            // Tree
    }
}
