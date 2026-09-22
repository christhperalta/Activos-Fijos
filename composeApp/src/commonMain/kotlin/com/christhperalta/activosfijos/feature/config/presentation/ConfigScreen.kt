package com.christhperalta.activosfijos.feature.config.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.config_btn_sing_in
import activosfijos.composeapp.generated.resources.quotation_back_description
import org.koin.compose.viewmodel.koinViewModel
import com.christhperalta.activosfijos.core.presentation.AppLogo
import com.christhperalta.activosfijos.core.presentation.CustomIconButton
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.feature.config.presentation.components.ManagerCredentialsSection
import com.christhperalta.activosfijos.feature.config.presentation.components.ServerSection
import com.christhperalta.activosfijos.feature.config.presentation.components.StoreSection
import com.christhperalta.activosfijos.feature.config.presentation.components.StoreSectionCallbacks
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: ConfigViewModel = koinViewModel(),
    onLogin: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,

        topBar = {

            TopAppBar(
                title = {},
                navigationIcon = {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        CustomIconButton(onClick = onLogin) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.quotation_back_description),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .align(alignment = Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AppLogo()

                ManagerCredentialsSection(
                    state = state,
                    onUsernameChange = {
                        viewModel.managerEvents(ConfigEvent.Manager.OnUsernameChange(it))
                    },
                    onPasswordChange = {
                        viewModel.managerEvents(ConfigEvent.Manager.OnPasswordChange(it))
                    },
                    onSave = { viewModel.managerEvents(ConfigEvent.Manager.SaveManagerConfig) }
                )


                ServerSection(
                    state = state,
                    onProtocolChange = {
                        viewModel.serverEvents(
                            ConfigEvent.Server.OnProtocolChange(
                                it
                            )
                        )
                    },
                    onPortChange = { viewModel.serverEvents(ConfigEvent.Server.OnPortChange(it)) },
                    onHostChange = { viewModel.serverEvents(ConfigEvent.Server.OnHostChange(it)) },
                    onDatabaseNameChange = {
                        viewModel.serverEvents(
                            ConfigEvent.Server.OnDatabaseNameChange(
                                it
                            )
                        )
                    },
                    onTestConnection = { viewModel.serverEvents(ConfigEvent.Server.TestConnection) },
                    onSave = { viewModel.serverEvents(ConfigEvent.Server.SaveConfig) },
                )

                StoreSection(
                    state = state,
                    callbacks = StoreSectionCallbacks(
                        onSellerChange = { name, code ->
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnSellerChange(
                                    name,
                                    code
                                )
                            )
                        },
                        onWarehouseChange = { name, code ->
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnWarehouseChange(
                                    name,
                                    code
                                )
                            )
                        },
                        onCustomerChange = { name, code ->
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnCustomerChange(
                                    name,
                                    code
                                )
                            )
                        },
                        onCardNameChange = {
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnCardNameChange(
                                    it
                                )
                            )
                        },
                        onDocCurrencyChange = {
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnDocCurrencyChange(
                                    it
                                )
                            )
                        },
                        onTaxCodeChange = {
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnTaxCodeChange(
                                    it
                                )
                            )
                        },
                        onTerminalNumberChange = {
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnTerminalNumberChange(
                                    it
                                )
                            )
                        },
                        onCardCodeChange = {
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnCardCodeChange(
                                    it
                                )
                            )
                        },
                        onPriceListChange = { name, code ->
                            viewModel.storageEvents(
                                ConfigEvent.Storage.OnPriceListChange(
                                    name,
                                    code
                                )
                            )
                        },
                        onRnc = { viewModel.storageEvents(ConfigEvent.Storage.OnRncChange(it)) },
                        onSave = { viewModel.storageEvents(ConfigEvent.Storage.SaveStoreConfig) },
                    )
                )

                state.error?.let {
                    CustomText(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                CustomPrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick =  onLogin
                ) {
                    CustomText(
                        text = stringResource(Res.string.config_btn_sing_in),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}
