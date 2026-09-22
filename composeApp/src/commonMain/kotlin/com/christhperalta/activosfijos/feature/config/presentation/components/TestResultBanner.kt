package com.christhperalta.activosfijos.feature.config.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.config_test_result_banner
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.core.theme.SuccessColor
import com.christhperalta.activosfijos.feature.config.presentation.TestResult
import org.jetbrains.compose.resources.stringResource

@Composable
 fun TestResultBanner(result: TestResult) {
    val color = when (result) {
        is TestResult.Success -> SuccessColor
        is TestResult.Error -> MaterialTheme.colorScheme.error
    }
    val icon = when (result) {
        is TestResult.Success -> Icons.Default.CheckCircle
        is TestResult.Error -> Icons.Default.Error
    }
    val msg = when (result) {
        is TestResult.Success -> stringResource(Res.string.config_test_result_banner)
        is TestResult.Error -> result.message
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp),
        )
        CustomText(
            text = msg,
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}