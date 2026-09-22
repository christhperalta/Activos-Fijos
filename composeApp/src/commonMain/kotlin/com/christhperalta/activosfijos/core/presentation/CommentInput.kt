package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.comment_input_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun CommentInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = Color(0xFF111827)),
            maxLines = 5,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = stringResource(Res.string.comment_input_placeholder), fontSize = 16.sp)
                }
                innerTextField()
            }
        )
    }
}