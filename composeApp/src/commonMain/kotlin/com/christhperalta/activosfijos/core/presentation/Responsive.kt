package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass



enum class LayoutType { MOBILE_PORTRAIT, TABLET_PORTRAIT, LANDSCAPE }

@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass,
    content: @Composable (LayoutType) -> Unit
) {

    val layoutType = when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            LayoutType.LANDSCAPE
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) ->
            LayoutType.TABLET_PORTRAIT
        else -> LayoutType.MOBILE_PORTRAIT
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        content(layoutType)
    }
}