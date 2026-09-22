package com.christhperalta.activosfijos.feature.onboarding.presentation

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.onboarding_btn_next
import activosfijos.composeapp.generated.resources.onboarding_btn_start
import com.christhperalta.activosfijos.core.presentation.AdaptiveLayout
import com.christhperalta.activosfijos.core.presentation.LayoutType
import com.christhperalta.activosfijos.feature.onboarding.presentation.components.CarouselSlide
import com.christhperalta.activosfijos.feature.onboarding.presentation.components.OnboardingBottomContent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    pages: List<OnboardingPage> = OnboardingDefaults.pages,
    onConfig: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val isLastPage = remember(currentPage) { currentPage == pages.lastIndex }
    val buttonText = if (isLastPage) stringResource(Res.string.onboarding_btn_start)
    else stringResource(Res.string.onboarding_btn_next)
    val goToNext: () -> Unit = { scope.launch { pagerState.animateScrollToPage(currentPage + 1) } }
    val uiState = remember(currentPage, isLastPage, buttonText) {
        OnboardingUiState(
            pages = pages,
            currentPage = currentPage,
            isLastPage = isLastPage,
            buttonText = buttonText,
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        AdaptiveLayout { layoutType ->
            when (layoutType) {
                LayoutType.MOBILE_PORTRAIT -> {
                    MobilePortraitOnboarding(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        pagerState = pagerState,
                        onNext = goToNext,
                        onConfig = onConfig,
                    )
                }
                LayoutType.TABLET_PORTRAIT -> {
                    PortraitOnboarding(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        pagerState = pagerState,
                        onNext = goToNext,
                        onConfig = onConfig,
                    )
                }

                LayoutType.LANDSCAPE -> {
                    LandscapeOnboarding(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        pagerState = pagerState,
                        onNext = goToNext,
                        onConfig = onConfig,
                    )
                }
            }

        }
    }
}



@Composable
private fun LandscapeOnboarding(
    uiState: OnboardingUiState,
    pagerState: PagerState,
    onNext: () -> Unit,
    onConfig: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxHeight()
        ) { pageIndex ->
            key(pageIndex) { CarouselSlide(page = uiState.pages[pageIndex]) }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OnboardingBottomContent(
                uiState = uiState,
                onNext = onNext,
                onConfig = onConfig,
            )
        }
    }
}



@Composable
private fun MobilePortraitOnboarding(
    uiState: OnboardingUiState,
    pagerState: PagerState,
    onNext: () -> Unit,
    onConfig: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.TopCenter)
        ) { pageIndex ->
            key(pageIndex) { CarouselSlide(page = uiState.pages[pageIndex]) }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingBottomContent(
                uiState = uiState,
                onNext = onNext,
                onConfig = onConfig,
            )
        }
    }
}


@Composable
private fun PortraitOnboarding(
    uiState: OnboardingUiState,
    pagerState: PagerState,
    onNext: () -> Unit,
    onConfig: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.TopCenter)
        ) { pageIndex ->
            key(pageIndex) { CarouselSlide(page = uiState.pages[pageIndex]) }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingBottomContent(
                uiState = uiState,
                onNext = onNext,
                onConfig = onConfig,
            )
        }
    }
}