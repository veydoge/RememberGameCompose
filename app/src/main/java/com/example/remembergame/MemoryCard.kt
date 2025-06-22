package com.example.remembergame

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.memorygame.Card
import kotlin.math.absoluteValue

@Composable
fun MemoryCard(card: Card, onClick: () -> Unit) {
    val rotation = animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(400)
    )

    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 8 * density
            }
            .clickable(enabled = !card.isMatched) { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(if (rotation.value <= 90f) Color.Gray else Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (rotation.value > 90f || card.isMatched) {
            Text(
                text = card.value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = "?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}