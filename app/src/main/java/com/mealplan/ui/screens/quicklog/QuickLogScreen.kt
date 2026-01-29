package com.mealplan.ui.screens.quicklog

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mealplan.domain.model.MealType
import com.mealplan.ui.components.CameraPreview
import com.mealplan.ui.components.LoadingButton
import com.mealplan.ui.theme.*
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QuickLogScreen(
    onNavigateBack: () -> Unit,
    viewModel: QuickLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCamera by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            showCamera = true
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    if (showCamera) {
        CameraPreview(
            onImageCaptured = { uri ->
                viewModel.onPhotoTaken(uri)
                showCamera = false
            },
            onError = { /* Handle error */ },
            onClose = { showCamera = false }
        )
        return
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Camera Permission Required") },
            text = { Text("We need camera access to take photos of your meals. Please grant permission to use this feature.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionRationale = false
                        cameraPermissionState.launchPermissionRequest()
                    }
                ) {
                    Text("Grant Permission")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Log") },
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
            item {
                Text(
                    text = "Log your meal in seconds",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = WarmCharcoal
                )
            }

            // Photo option or captured photo
            item {
                if (uiState.photoUri != null) {
                    CapturedPhotoCard(
                        photoUri = uiState.photoUri.toString(),
                        onRetake = {
                            viewModel.clearPhoto()
                            if (cameraPermissionState.status.isGranted) {
                                showCamera = true
                            } else if (cameraPermissionState.status.shouldShowRationale) {
                                showPermissionRationale = true
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                        onClear = { viewModel.clearPhoto() }
                    )
                } else {
                    LogOptionCard(
                        icon = Icons.Outlined.CameraAlt,
                        title = "Take a photo",
                        description = "Snap a pic of your meal",
                        onClick = {
                            when {
                                cameraPermissionState.status.isGranted -> {
                                    showCamera = true
                                }
                                cameraPermissionState.status.shouldShowRationale -> {
                                    showPermissionRationale = true
                                }
                                else -> {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    )
                }
            }

            // Voice option
            item {
                LogOptionCard(
                    icon = Icons.Outlined.Mic,
                    title = "Voice note",
                    description = "Say what you ate",
                    onClick = { /* Voice integration */ },
                    comingSoon = true
                )
            }

            // Meal type selector
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "What meal is this?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmCharcoal
                )
            }

            item {
                MealTypeSelector(
                    selectedType = uiState.selectedMealType,
                    onTypeSelected = viewModel::selectMealType
                )
            }

            // Quick pick from recent
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Or quick pick:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmCharcoal
                )
            }

            items(getQuickPickOptions()) { option ->
                QuickPickItem(
                    name = option,
                    onClick = { viewModel.selectQuickPick(option) },
                    isSelected = uiState.selectedQuickPick == option
                )
            }

            // Notes
            item {
                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = viewModel::updateNotes,
                    label = { Text("Add a note (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Log button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                LoadingButton(
                    text = "Log Meal",
                    isLoading = uiState.isLoading,
                    onClick = viewModel::logMeal,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedMealType != null && uiState.selectedQuickPick != null
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CapturedPhotoCard(
    photoUri: String,
    onRetake: () -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftLavenderTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Captured meal photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear")
                }
                Button(
                    onClick = onRetake,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftSageGreen)
                ) {
                    Text("Retake")
                }
            }
        }
    }
}

@Composable
private fun LogOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    comingSoon: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !comingSoon, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (comingSoon) SoftWhite.copy(alpha = 0.5f) else SoftLavenderTint
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftSageGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = SoftSageGreen
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (comingSoon) SoftGray else WarmCharcoal
                    )
                    if (comingSoon) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Coming soon",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoftCoral
                        )
                    }
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGray
                )
            }
        }
    }
}

@Composable
private fun MealTypeSelector(
    selectedType: MealType?,
    onTypeSelected: (MealType) -> Unit
) {
    val suggestedType = getSuggestedMealType()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MealType.entries.filter { it != MealType.SNACK }.forEach { type ->
            MealTypeChip(
                type = type,
                isSelected = selectedType == type,
                isSuggested = type == suggestedType,
                onClick = { onTypeSelected(type) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MealTypeChip(
    type: MealType,
    isSelected: Boolean,
    isSuggested: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emoji = when (type) {
        MealType.BREAKFAST -> "\uD83C\uDF73"
        MealType.LUNCH -> "\uD83E\uDD57"
        MealType.DINNER -> "\uD83C\uDF5D"
        MealType.SNACK -> "\uD83C\uDF4E"
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) SoftSageGreen else SoftWhite
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleLarge)
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) White else WarmCharcoal
        )
        if (isSuggested && !isSelected) {
            Text(
                text = "suggested",
                style = MaterialTheme.typography.labelSmall,
                color = SoftCoral
            )
        }
    }
}

@Composable
private fun QuickPickItem(
    name: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SoftSageGreen.copy(alpha = 0.2f) else SoftWhite
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = WarmCharcoal,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Selected",
                    tint = SoftSageGreen
                )
            }
        }
    }
}

private fun getSuggestedMealType(): MealType {
    val hour = LocalTime.now().hour
    return when {
        hour < 11 -> MealType.BREAKFAST
        hour < 15 -> MealType.LUNCH
        else -> MealType.DINNER
    }
}

private fun getQuickPickOptions(): List<String> {
    return listOf(
        "Salad with protein",
        "Grilled chicken and vegetables",
        "Sandwich or wrap",
        "Bowl (rice/grain based)",
        "Soup or stew",
        "Eggs and toast",
        "Smoothie",
        "Leftovers",
        "Something else healthy"
    )
}
