package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_total_box_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun TotalBox(total: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFDCFCE7),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 0.5.dp,
                color = Color(0xFF16A34A).copy(alpha = 0.3f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(Res.string.home_total_box_text),
                fontSize = 11.sp,
                color = Color(0xFF16A34A)
            )
            Text(
                text = total,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF16A34A)
            )
        }
    }
}