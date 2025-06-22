package com.example.memorygame

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.remembergame.MemoryCard
import kotlinx.coroutines.delay

data class Card(
    val id: Int,
    val value: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

val cardValues = listOf("🍎", "🍌", "🍒", "🍇", "🍉", "🍓")
val cardList = (cardValues + cardValues).shuffled().mapIndexed { index, value ->
    Card(id = index, value = value)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen() {
    var cards by remember { mutableStateOf(cardList) }
    var flippedCards by remember { mutableStateOf(listOf<Card>()) }
    var blockInput by remember { mutableStateOf(false) }

    // Сравнение карт при открытии двух
    LaunchedEffect(flippedCards) {
        if (flippedCards.size == 2) {
            val first = flippedCards[0]
            val second = flippedCards[1]
            blockInput = true

            if (first.value == second.value) {
                cards = cards.map {
                    if (it.value == first.value) it.copy(isMatched = true)
                    else it
                }
            } else {
                delay(800)
                cards = cards.map {
                    if (it.id == first.id || it.id == second.id) it.copy(isFlipped = false)
                    else it
                }
            }

            flippedCards = emptyList()
            blockInput = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8FF)),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                MemoryCard(
                    card = card,
                    onClick = {
                        if (blockInput || card.isFlipped || card.isMatched || flippedCards.size == 2) return@MemoryCard

                        val newCard = card.copy(isFlipped = true)
                        val newCards = cards.toMutableList()
                        newCards[index] = newCard
                        cards = newCards
                        flippedCards = flippedCards + newCard
                    }
                )
            }
        }
    }
}