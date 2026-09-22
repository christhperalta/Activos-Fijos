package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.christhperalta.activosfijos.core.presentation.AppLogo

@Composable
fun B1PosLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AppLogo(width = 180, height = 90)
    }
}