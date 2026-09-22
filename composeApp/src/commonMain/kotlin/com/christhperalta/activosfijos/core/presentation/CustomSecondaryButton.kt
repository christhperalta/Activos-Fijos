package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text : String,
    onClick : () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .height(48.dp),
        onClick = { onClick() },

        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.secondary,
        )
    ) {
        CustomText(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}
