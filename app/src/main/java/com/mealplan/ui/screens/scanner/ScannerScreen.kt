package com.mealplan.ui.screens.scanner

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mealplan.domain.model.Recipe
import com.mealplan.ui.components.CameraPreview
import com.mealplan.ui.components.DifficultyIndicator
import com.mealplan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRecipe: (String) -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCamera by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            showCamera = true
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
            text = { Text("We need camera access to scan your fridge and identify ingredients. Please grant permission to use this feature.") },
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
                title = { Text("What can I make?") },
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
            // Camera scan card or captured photo
            item {
                if (uiState.capturedPhotoUri != null) {
                    CapturedPhotoCard(
                        photoUri = uiState.capturedPhotoUri.toString(),
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
                    CameraScanCard(
                        onScanClick = {
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

            // Quick ingredient picker
            item {
                Text(
                    text = "Or select ingredients you have:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmCharcoal
                )
            }

            // Common ingredients
            item {
                IngredientPicker(
                    selectedIngredients = uiState.selectedIngredients,
                    onIngredientToggle = viewModel::toggleIngredient
                )
            }

            // Search results
            if (uiState.suggestedRecipes.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Here's what you can make:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmCharcoal
                    )
                }

                items(uiState.suggestedRecipes) { recipe ->
                    SuggestedRecipeCard(
                        recipe = recipe,
                        matchingIngredients = uiState.selectedIngredients.size,
                        onClick = { onNavigateToRecipe(recipe.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CameraScanCard(
    onScanClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onScanClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftLavenderTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = "Scan fridge",
                modifier = Modifier.size(48.dp),
                tint = SoftSageGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Scan your fridge",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = WarmCharcoal
            )

            Text(
                text = "Take a photo and we'll identify ingredients",
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray
            )
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
                contentDescription = "Captured photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Photo captured! AI analysis coming soon.",
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray,
                textAlign = TextAlign.Center
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
                    Text("Retake Photo")
                }
            }
        }
    }
}

@Composable
private fun IngredientPicker(
    selectedIngredients: Set<String>,
    onIngredientToggle: (String) -> Unit
) {
    val ingredientCategories = mapOf(
        "Proteins" to listOf("Chicken", "Beef", "Pork", "Fish", "Eggs", "Tofu", "Shrimp"),
        "Vegetables" to listOf("Broccoli", "Spinach", "Tomatoes", "Onions", "Peppers", "Carrots", "Garlic"),
        "Carbs" to listOf("Rice", "Pasta", "Bread", "Potatoes", "Quinoa", "Oats"),
        "Dairy" to listOf("Milk", "Cheese", "Yogurt", "Butter"),
        "Pantry" to listOf("Olive oil", "Soy sauce", "Honey", "Lemon", "Salt", "Pepper")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ingredientCategories.forEach { (category, ingredients) ->
            Column {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium,
                    color = SoftGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ingredients) { ingredient ->
                        IngredientChip(
                            name = ingredient,
                            isSelected = selectedIngredients.contains(ingredient),
                            onClick = { onIngredientToggle(ingredient) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(name) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SoftSageGreen,
            selectedLabelColor = White
        )
    )
}

@Composable
private fun SuggestedRecipeCard(
    recipe: Recipe,
    matchingIngredients: Int,
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
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftSageGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = recipe.name.take(1),
                    style = MaterialTheme.typography.headlineSmall,
                    color = SoftSageGreen
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmCharcoal
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = SoftGray
                    )
                    Text(
                        text = "${recipe.totalTimeMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftGray
                    )
                    DifficultyIndicator(difficulty = recipe.difficulty)
                }

                if (recipe.isEasyMeal) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\u2728 Easy recipe",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftCoral
                    )
                }
            }
        }
    }
}
