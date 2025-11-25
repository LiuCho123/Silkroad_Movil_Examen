package com.example.hollow_knight_silkroad.View.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.* // Import common icons
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val label: String, val icon: ImageVector) {
    object Foro : NavigationItem("foro", "Foro", Icons.Filled.Forum)
    object Guia : NavigationItem("guia", "Guía", Icons.AutoMirrored.Filled.MenuBook)
    object Checklist : NavigationItem("checklist", "Checklist", Icons.Filled.Checklist)
    object Ranking : NavigationItem("ranking", "Ranking", Icons.Filled.EmojiEvents)
    object Trivia : NavigationItem("trivia", "Trivia", Icons.Filled.Quiz)
}

val bottomNavItems = listOf(
    NavigationItem.Foro,
    NavigationItem.Guia,
    NavigationItem.Checklist,
    NavigationItem.Ranking,
    NavigationItem.Trivia
)