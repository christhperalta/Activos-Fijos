package com.christhperalta.activosfijos.feature.onboarding.presentation

data class OnboardingUiState(
    val pages: List<OnboardingPage>,
    val currentPage: Int,
    val isLastPage: Boolean,
    val buttonText: String,
)