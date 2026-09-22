package com.christhperalta.activosfijos.feature.login.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.login_config_button
import com.christhperalta.activosfijos.core.presentation.CustomText
import org.jetbrains.compose.resources.stringResource

@Composable
 fun ConfigButton(
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        onClick = onClick,
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.secondary
        )
    ) {
        CustomText(
            text = stringResource(Res.string.login_config_button),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}