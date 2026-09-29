package com.christhperalta.activosfijos.feature.home.presentation

import androidx.compose.runtime.Composable
import com.christhperalta.activosfijos.core.shared.UserNameRepository
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryHomeScreen
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onScanner: () -> Unit,
    onSummary: () -> Unit,
    onAssets: () -> Unit,
    userNameRepository: UserNameRepository = koinInject(),
) {
    val userName = runCatching { userNameRepository.getName() }.getOrDefault("Juan Peralta")
    InventoryHomeScreen(
        userName = userName,
        onScanner = onScanner,
        onSummary = onSummary,
        onAssets = onAssets,
    )
}
