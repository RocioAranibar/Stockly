package com.stockly.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Forest = Color(0xFF284D35)
val ForestDark = Color(0xFF183524)
val Cream = Color(0xFFF7F3EA)
val StocklyCard = Color(0xFFFFFCF6)
val Muted = Color(0xFF76756F)
val Warning = Color(0xFFE58B2A)
val Danger = Color(0xFFD94B4B)
val Success = Color(0xFF3A7D55)

private val StocklyColors = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDEBDD),
    onPrimaryContainer = ForestDark,
    secondary = Color(0xFF6F806E),
    background = Cream,
    onBackground = Color(0xFF1D211E),
    surface = StocklyCard,
    onSurface = Color(0xFF1D211E),
    error = Danger
)

@Composable
fun StocklyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = StocklyColors, content = content)
}
