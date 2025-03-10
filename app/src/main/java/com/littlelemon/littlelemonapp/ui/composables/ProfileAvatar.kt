package com.littlelemon.littlelemonapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Displays a circular avatar. it shows the user's initials (ex: "SA" for "Sarah Anderson").
 */
@Composable
fun ProfileAvatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    size: Int = 80,
    displayInitialAt: String = "profile"
) {
    // Split name into parts to get initials
    val firstInitial = firstName.firstOrNull()?.uppercaseChar()?.toString() ?: ""
    val secondInitial = lastName.firstOrNull()?.uppercaseChar()?.toString() ?: ""
    val initials = (firstInitial + secondInitial).takeIf { it.isNotBlank() } ?: "?"
    val initialStyle = when (displayInitialAt) {
        "profile" -> MaterialTheme.typography.displaySmall
        else -> {
            MaterialTheme.typography.bodyMedium
        }
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = initialStyle.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}