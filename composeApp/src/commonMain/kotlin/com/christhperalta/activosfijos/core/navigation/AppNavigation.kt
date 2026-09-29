package com.christhperalta.activosfijos.core.navigation


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.christhperalta.activosfijos.feature.config.presentation.SetupScreen
import com.christhperalta.activosfijos.feature.home.presentation.HomeScreen
import com.christhperalta.activosfijos.feature.login.presentation.LoginScreen
import com.christhperalta.activosfijos.feature.onboarding.presentation.OnboardingScreen
import com.christhperalta.activosfijos.feature.pdf.presentation.PdfScreen
import com.christhperalta.activosfijos.feature.product.presentation.ProductDetailScreen
import com.christhperalta.activosfijos.feature.scanner.presentation.ScannerScreen

private fun NavBackStack<NavKey>.popUntilHomeIsTop() {
    while (true) {
        when (lastOrNull()) {
            null -> return
            is Home -> return
            else -> removeLast()
        }
    }
}

private val ScaleFadeEnter: EnterTransition =
    fadeIn(animationSpec = tween(400, easing = EaseOutCubic)) +
            scaleIn(
                initialScale = 0.75f,                                  // entra desde más lejos
                animationSpec = tween(400, easing = EaseOutCubic)
            )

private val ScaleFadeExit: ExitTransition =
    fadeOut(animationSpec = tween(300, easing = EaseInCubic)) +
            scaleOut(
                targetScale = 1.10f,                                   // sale "acercándose" a la cámara
                animationSpec = tween(300, easing = EaseInCubic)
            )

private val ScaleFadePopEnter: EnterTransition =
    fadeIn(animationSpec = tween(400, easing = EaseOutCubic)) +
            scaleIn(
                initialScale = 1.10f,                                  // al volver, entra desde cerca
                animationSpec = tween(400, easing = EaseOutCubic)
            )

private val ScaleFadePopExit: ExitTransition =
    fadeOut(animationSpec = tween(300, easing = EaseInCubic)) +
            scaleOut(
                targetScale = 0.75f,                                   // sale alejándose
                animationSpec = tween(300, easing = EaseInCubic)
            )


@Composable
fun AppNavigation(startKey: NavKey) {
    val backStack = rememberNavBackStack(navConfig, Home)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLast() },

        transitionSpec = {
            ScaleFadeEnter togetherWith ScaleFadeExit
        },
        popTransitionSpec = {
            ScaleFadePopEnter togetherWith ScaleFadePopExit
        },

        entryProvider = { key ->
            when (key) {
                is Onboarding -> NavEntry(key) {
                    OnboardingScreen(
                        onConfig = {
                            backStack.removeLastOrNull()
                            backStack.add(Config)
                        }
                    )
                }

                is Login -> NavEntry(key) {
                    LoginScreen(
                        onHome = { backStack.add(Home) },
                        onConfig = { backStack.add(Config) }
                    )
                }

                is Config -> NavEntry(key) {
                    SetupScreen(
                        onLogin = {
                            backStack.removeLastOrNull()
                            backStack.add(Login)
                        },
                    )
                }

                is Home -> NavEntry(key) {
                    HomeScreen(
                        onLogin = {
                            backStack.removeLastOrNull()
                            backStack.add(Login)
                        },
                        onScanner = { backStack.add(Scanner) },
                        onProduct = { productId , productBarCode ->
                            backStack.add(Product(productId = productId, productBarCode = productBarCode))
                        },
                        onSearchProduct = {
                            backStack.add(SearchProduct)
                        },
                        onQuotation = { backStack.add(Quotation) }
                    )
                }

                is Scanner -> NavEntry(key) {
                    ScannerScreen(
                        onProduct = { productId, productBarCode ->
                            backStack.add(Product(productId = productId, productBarCode = productBarCode))
                        }
                    )
                }

                is Product -> NavEntry(key) {
                    ProductDetailScreen(
                        productId = key.productId,
                        onBack = { backStack.popUntilHomeIsTop() },
                        onLogin = {
                            backStack.removeLastOrNull()
                            backStack.add(Login)
                        },
                    )
                }


                is Pdf -> NavEntry(key) {
                    PdfScreen(
                        url = key.pdfUrl
                    )
                }



                else -> throw IllegalArgumentException("Unknown NavKey: $key")
            }
        }
    )
}