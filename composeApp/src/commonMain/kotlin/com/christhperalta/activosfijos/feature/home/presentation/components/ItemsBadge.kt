package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_items_badge
import org.jetbrains.compose.resources.stringResource

@Composable
fun ItemsBadge(count: Int) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$count ${stringResource(Res.string.home_items_badge )} ",
            fontSize = 11.sp,
            color = Color(0xFF1D4ED8)
        )
    }
}