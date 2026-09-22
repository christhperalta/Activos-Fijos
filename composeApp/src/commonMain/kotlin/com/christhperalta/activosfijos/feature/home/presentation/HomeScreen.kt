package com.christhperalta.activosfijos.feature.home.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.activosfijos.core.presentation.SessionExpiredAlertDialog
import com.christhperalta.activosfijos.core.quotation.QuotationCart
import com.christhperalta.activosfijos.feature.home.presentation.components.*
import org.koin.compose.koinInject


@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onScanner: () -> Unit,
    onProduct: (String?,String?) -> Unit,
    onQuotation: () -> Unit,
    quotationCart: QuotationCart = koinInject(),
    viewModel: HomeViewModel = koinInject(),
    onLogin: () -> Unit,
    onSearchProduct: () -> Unit,
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("Juan Peralta") },
                actions = {}
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onScanner,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scanner"
                        )
                    },
                    label = { Text("Scanner") }
                )
            }
        }
    ) { innerPadding ->

     Column(modifier = Modifier.padding(innerPadding)) {
         Text("Home")
     }
}}
