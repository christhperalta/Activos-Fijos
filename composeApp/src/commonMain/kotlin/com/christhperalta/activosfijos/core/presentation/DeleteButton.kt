package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeleteButton(onClick: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (hovered) Color(0xFFFEE2E2) else Color.Transparent,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = if (hovered) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.35f
            ),
            modifier = Modifier.size(22.dp)
        )
    }
}