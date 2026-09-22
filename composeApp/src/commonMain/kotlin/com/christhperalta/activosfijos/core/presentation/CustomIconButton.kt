package com.christhperalta.activosfijos.core.presentation


import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    containerColor  : Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    IconButton(
        enabled = enabled,
        modifier = modifier, onClick = {onClick()}, colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,

            )
    ) {
        content()
    }

}
