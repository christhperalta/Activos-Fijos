package com.christhperalta.activosfijos.feature.onboarding.presentation

import androidx.compose.ui.graphics.Color
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.img_1
import activosfijos.composeapp.generated.resources.img_2
import activosfijos.composeapp.generated.resources.img_3
import activosfijos.composeapp.generated.resources.onboarding_sub_1
import activosfijos.composeapp.generated.resources.onboarding_sub_2
import activosfijos.composeapp.generated.resources.onboarding_sub_3
import activosfijos.composeapp.generated.resources.onboarding_title_1
import activosfijos.composeapp.generated.resources.onboarding_title_2
import activosfijos.composeapp.generated.resources.onboarding_title_3

object OnboardingDefaults {
    val pages = listOf(
        OnboardingPage(
            img = Res.drawable.img_1,
            bgTint = Color(0xFFB5D5C5),
            titleRes = Res.string.onboarding_title_1,
            subtitleRes = Res.string.onboarding_sub_1,
        ),
        OnboardingPage(
            img = Res.drawable.img_2,
            bgTint = Color(0xFFB5C8D5),
            titleRes = Res.string.onboarding_title_2,
            subtitleRes = Res.string.onboarding_sub_2,
        ),
        OnboardingPage(
            img = Res.drawable.img_3,
            bgTint = Color(0xFFD5B5C8),
            titleRes = Res.string.onboarding_title_3,
            subtitleRes = Res.string.onboarding_sub_3,
        ),
    )
}