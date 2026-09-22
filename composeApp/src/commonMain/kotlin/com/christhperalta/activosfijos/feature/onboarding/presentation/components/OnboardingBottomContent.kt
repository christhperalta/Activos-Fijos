package com.christhperalta.activosfijos.feature.onboarding.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.onboarding_btn_skip
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.core.presentation.SecondaryButton
import com.christhperalta.activosfijos.feature.onboarding.presentation.OnboardingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingBottomContent(
    uiState: OnboardingUiState,
    onNext: () -> Unit,
    onConfig: () -> Unit,
) {
    PaginationDots(
        currentPage = uiState.currentPage,
        totalPages = uiState.pages.size,
        modifier = Modifier.padding(vertical = 16.dp)
    )
    CustomText(
        text = stringResource(uiState.pages[uiState.currentPage].titleRes),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        lineHeight = 38.sp,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    CustomText(
        text = stringResource(uiState.pages[uiState.currentPage].subtitleRes),
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = 21.sp,
        modifier = Modifier.padding(horizontal = 40.dp)
    )
    Spacer(modifier = Modifier.height(28.dp))

    CustomPrimaryButton(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        onClick = if (uiState.isLastPage) onConfig else onNext) {
        CustomText(
            text = uiState.buttonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.background
        )
    }
    if (!uiState.isLastPage) {
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = stringResource(Res.string.onboarding_btn_skip),
            onClick = onConfig
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
}