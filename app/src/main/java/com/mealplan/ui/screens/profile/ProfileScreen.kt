package com.mealplan.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplan.domain.model.UserProgress
import com.mealplan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWellness: () -> Unit,
    onNavigateToBenefits: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSignedOut) {
        if (uiState.isSignedOut) {
            onSignOut()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile header
            item {
                ProfileHeader(
                    name = uiState.userName,
                    email = uiState.email
                )
            }

            // Progress stats
            item {
                ProgressCard(progress = uiState.progress)
            }

            // Wellness section
            item {
                SectionHeader(title = "Wellness")
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.SelfImprovement,
                    title = "Weekly Check-in",
                    description = "Track how you feel",
                    onClick = onNavigateToWellness
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Timeline,
                    title = "Benefits Timeline",
                    description = "See what to expect",
                    onClick = onNavigateToBenefits
                )
            }

            // Settings section
            item {
                SectionHeader(title = "Settings")
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Person,
                    title = "Edit Preferences",
                    description = "Goals, restrictions, skill level",
                    onClick = { /* Navigate to edit preferences */ }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    description = "Manage reminders",
                    onClick = { /* Navigate to notifications */ }
                )
            }

            // Sign out
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItem(
                    icon = Icons.AutoMirrored.Outlined.ExitToApp,
                    title = "Sign Out",
                    description = "See you soon!",
                    onClick = viewModel::signOut,
                    isDestructive = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    email: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SoftSageGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.headlineMedium,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = WarmCharcoal
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = SoftGray
            )
        }
    }
}

@Composable
private fun ProgressCard(progress: UserProgress?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftLavenderTint)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Your Journey",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = (progress?.totalHealthyDays ?: 0).toString(),
                    label = "Healthy Days",
                    emoji = "\uD83C\uDF1F"
                )
                StatItem(
                    value = (progress?.totalCheckIns ?: 0).toString(),
                    label = "Check-ins",
                    emoji = "\u2705"
                )
                StatItem(
                    value = (progress?.recipesCompleted ?: 0).toString(),
                    label = "Recipes",
                    emoji = "\uD83C\uDF7D\uFE0F"
                )
            }

            if ((progress?.longestStreak ?: 0) > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MintGreen.copy(alpha = 0.3f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\uD83C\uDFC6",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Personal Best",
                            style = MaterialTheme.typography.labelMedium,
                            color = SoftGray
                        )
                        Text(
                            text = "${progress?.longestStreak} day streak!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = WarmCharcoal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
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
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = SoftGray,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDestructive) SoftCoral else SoftSageGreen
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDestructive) SoftCoral else WarmCharcoal
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGray
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = LightGray
            )
        }
    }
}
