package com.christhperalta.activosfijos.feature.login.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.login_header_subtitle
import activosfijos.composeapp.generated.resources.login_header_welcome
import com.christhperalta.activosfijos.core.presentation.AppLogo
import com.christhperalta.activosfijos.core.presentation.CustomText
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo()
        Spacer(modifier = Modifier.height(20.dp))
        CustomText(
            text = stringResource(Res.string.login_header_welcome),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp,
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomText(
            text = stringResource(Res.string.login_header_subtitle),
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(13.dp))
    }
}