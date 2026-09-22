package com.christhperalta.activosfijos.feature.onboarding.presentation

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnboardingPage(
    val img: DrawableResource,
    val bgTint: Color,
    val titleRes: StringResource,
    val subtitleRes: StringResource,
)
