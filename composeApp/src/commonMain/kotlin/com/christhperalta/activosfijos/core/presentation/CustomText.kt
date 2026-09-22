package com.christhperalta.activosfijos.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text : String,
    fontWeight : FontWeight = FontWeight.Normal,
    color : Color = MaterialTheme.colorScheme.onSurface,
    fontSize : TextUnit = TextUnit.Unspecified,
    textAlign : TextAlign = TextAlign.Center,
    lineHeight : TextUnit = 30.sp,
    style : TextStyle =  MaterialTheme.typography.bodyLarge
) {
    Text(
        text       = text,
        fontWeight = fontWeight,
        color      = color,
        textAlign  = textAlign,
        lineHeight = lineHeight,
        modifier   = modifier,
        style      = style,
        fontSize   = fontSize
    )
}

