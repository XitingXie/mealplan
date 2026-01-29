package com.mealplan.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.mealplan.ui.theme.SoftSageGreen
import com.mealplan.ui.theme.WarmPeach

@Composable
fun LoadingIndicator(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PulsingDots()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PulsingDots(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing")

    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale1"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale2"
    )

    val scale3 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .scale(scale1)
                .clip(CircleShape)
                .background(SoftSageGreen)
        )
        Box(
            modifier = Modifier
                .size(16.dp)
                .scale(scale2)
                .clip(CircleShape)
                .background(WarmPeach)
        )
        Box(
            modifier = Modifier
                .size(16.dp)
                .scale(scale3)
                .clip(CircleShape)
                .background(SoftSageGreen)
        )
    }
}

@Composable
fun FullScreenLoading(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(message = message)
    }
}

@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(28.dp)
    ) {
        if (isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallPulsingDots()
            }
        } else {
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SmallPulsingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "small_pulsing")

    val scales = (0..2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(400, delayMillis = index * 100, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "smallScale$index"
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        scales.forEach { scale ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}
