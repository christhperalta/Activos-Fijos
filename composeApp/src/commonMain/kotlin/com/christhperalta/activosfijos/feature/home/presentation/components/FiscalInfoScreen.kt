package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_fiscal_info_client_data_title
import activosfijos.composeapp.generated.resources.home_fiscal_info_close
import activosfijos.composeapp.generated.resources.home_fiscal_info_document_type_label
import activosfijos.composeapp.generated.resources.home_fiscal_info_error_title
import activosfijos.composeapp.generated.resources.home_fiscal_info_id_card
import activosfijos.composeapp.generated.resources.home_fiscal_info_name_label
import activosfijos.composeapp.generated.resources.home_fiscal_info_name_placeholder
import activosfijos.composeapp.generated.resources.home_fiscal_info_ncf_consumer_final
import activosfijos.composeapp.generated.resources.home_fiscal_info_ncf_credito_fiscal
import activosfijos.composeapp.generated.resources.home_fiscal_info_ncf_type_label
import activosfijos.composeapp.generated.resources.home_fiscal_info_observations_title
import activosfijos.composeapp.generated.resources.home_fiscal_info_passport
import activosfijos.composeapp.generated.resources.home_fiscal_info_quotation
import activosfijos.composeapp.generated.resources.home_fiscal_info_rnc
import activosfijos.composeapp.generated.resources.home_fiscal_info_sales_order
import activosfijos.composeapp.generated.resources.home_fiscal_info_send_to_sap
import activosfijos.composeapp.generated.resources.home_fiscal_info_success_description
import activosfijos.composeapp.generated.resources.home_fiscal_info_success_text
import activosfijos.composeapp.generated.resources.home_fiscal_info_success_title
import activosfijos.composeapp.generated.resources.home_fiscal_info_title
import activosfijos.composeapp.generated.resources.home_fiscal_info_tributary_title
import com.christhperalta.activosfijos.core.presentation.CommentInput
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialog
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialogActions
import com.christhperalta.activosfijos.core.presentation.CustomDropdownField
import com.christhperalta.activosfijos.core.presentation.CustomIconButton
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.feature.home.presentation.CreateQuotationParams
import com.christhperalta.activosfijos.feature.home.presentation.DocumentType
import com.christhperalta.activosfijos.feature.home.presentation.HomeEvent
import com.christhperalta.activosfijos.feature.home.presentation.HomeUiState
import com.christhperalta.activosfijos.feature.home.presentation.QuotationResult
import com.christhperalta.activosfijos.feature.home.presentation.Tax_Credit
import com.christhperalta.activosfijos.core.quotation.Product
import org.jetbrains.compose.resources.stringResource


data class FiscalInfoActions(
    val onEvent: (HomeEvent) -> Unit,
    val onClose: () -> Unit,
    val onQuotationConfirmed: () -> Unit,
    val onSelectedDocumentTypeChange: (DocumentType) -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiscalInfoScreen(
    uiState: HomeUiState,
    quotationLines: List<Product>,
    selectedDocumentType: DocumentType,
    quotationResult: QuotationResult = QuotationResult.Idle,
    actions: FiscalInfoActions
) {
    val ncfCode =
        if (uiState.ncfType == stringResource(Res.string.home_fiscal_info_ncf_consumer_final)) "32" else "31"
    val businessType = when (selectedDocumentType) {
        DocumentType.RNC -> "1"
        DocumentType.CEDULA -> "2"
        DocumentType.PASAPORTE -> "3"
    }
    val canSend = uiState.ncfType.isNotEmpty()
            && uiState.documentType.isNotEmpty()
            && quotationLines.isNotEmpty()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    CustomText(
                        text = stringResource(Res.string.home_fiscal_info_title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        CustomIconButton(onClick = actions.onClose) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.home_fiscal_info_close),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        if (canSend) {
                            actions.onEvent(
                                HomeEvent.OnCreateQuotation(
                                    params = CreateQuotationParams(
                                        comment = uiState.comment,
                                        cardName = uiState.name,
                                        facSerie = ncfCode,
                                        products = quotationLines,
                                        rnc = uiState.rncValidated?.rnc?.takeIf { it.isNotEmpty() }
                                            ?: uiState.rnc,
                                        businessType = businessType,
                                        endPoint = if (uiState.documentType == "Cotización") "Quotations" else "Orders"
                                    )
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = canSend
                ) {
                    CustomText(
                        text = stringResource(Res.string.home_fiscal_info_send_to_sap),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TributaryInfoCard(uiState = uiState, onEvent = actions.onEvent)
            ClientFiscalDataCard(
                uiState = uiState,
                selectedDocumentType = selectedDocumentType,
                onSelectedDocumentTypeChange = actions.onSelectedDocumentTypeChange,
                onEvent = actions.onEvent
            )
            ObservationsCard(
                comment = uiState.comment,
                onCommentChange = { actions.onEvent(HomeEvent.OnCommentChange(it)) }
            )
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    when (quotationResult) {
        is QuotationResult.Success -> {
            CustomAlertDialog(
                actions = CustomAlertDialogActions(
                    onDismissRequest = {},
                    enableBottomDismiss = false,
                    onConfirmation = actions.onQuotationConfirmed,
                ),
                icon = Icons.Default.Done,
                dialogTitle = stringResource(Res.string.home_fiscal_info_success_title),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(text = stringResource(Res.string.home_fiscal_info_success_description))
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText(
                        text = quotationResult.docNum,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CustomText(text = stringResource(Res.string.home_fiscal_info_success_text))
                }
            }
        }
        is QuotationResult.Error -> {
            CustomAlertDialog(
                actions = CustomAlertDialogActions(
                    onDismissRequest = {},
                    enableBottomDismiss = false,
                    onConfirmation = actions.onQuotationConfirmed,
                ),
                icon = Icons.Default.Close,
                dialogTitle = stringResource(Res.string.home_fiscal_info_error_title),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CustomText(text = quotationResult.message)
                }
            }
        }
        QuotationResult.Idle -> {}
    }

}

// ── Subcomponentes privados del panel ────────────────────────────────────────

@Composable
private fun TributaryInfoCard(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit
) {
    FiscalCard(
        title = stringResource(Res.string.home_fiscal_info_tributary_title)
    ) {
        CustomDropdownField(
            label = stringResource(Res.string.home_fiscal_info_ncf_type_label),
            selected = uiState.ncfType,
            options = listOf(
                stringResource(Res.string.home_fiscal_info_ncf_consumer_final),
                stringResource(Res.string.home_fiscal_info_ncf_credito_fiscal)
            ),
            onSelect = { onEvent(HomeEvent.OnNcfChange(it)) },
            itemLabel = { it }
        )
        CustomDropdownField(
            label = stringResource(Res.string.home_fiscal_info_document_type_label),
            selected = uiState.documentType,
            options = listOf(
                stringResource(Res.string.home_fiscal_info_sales_order),
                stringResource(Res.string.home_fiscal_info_quotation)
            ),
            onSelect = { onEvent(HomeEvent.OnDocumentTypeChange(it)) },
            itemLabel = { it }
        )
    }
}

@Composable
private fun ClientFiscalDataCard(
    uiState: HomeUiState,
    selectedDocumentType: DocumentType,
    onSelectedDocumentTypeChange: (DocumentType) -> Unit,
    onEvent: (HomeEvent) -> Unit
) {
    FiscalCard(title = stringResource(Res.string.home_fiscal_info_client_data_title)) {
        if (uiState.ncfType == stringResource(Res.string.home_fiscal_info_ncf_credito_fiscal)) {
            DocumentTypeSelector(
                selectedType = selectedDocumentType,
                onTypeSelected = onSelectedDocumentTypeChange
            )
            RncInput(
                value = uiState.rnc,
                onValueChange = { onEvent(HomeEvent.OnRncChange(it)) },
                onSearch = { onEvent(HomeEvent.OnValidateRnc(uiState.rnc)) },
                icon = when (selectedDocumentType) {
                    DocumentType.CEDULA -> Icons.Default.Badge
                    else -> Icons.Default.Person
                },
                label = when (selectedDocumentType) {
                    DocumentType.RNC -> stringResource(Res.string.home_fiscal_info_rnc)
                    DocumentType.CEDULA -> stringResource(Res.string.home_fiscal_info_id_card)
                    DocumentType.PASAPORTE -> stringResource(Res.string.home_fiscal_info_passport)
                }
            )
        }
        CustomInput(
            value = uiState.name,
            onValueChange = { onEvent(HomeEvent.OnNameChange(it)) },
            config = CustomInputConfig(
                readOnly = uiState.ncfType == Tax_Credit,
                label = stringResource(Res.string.home_fiscal_info_name_label),
                placeholder = stringResource(Res.string.home_fiscal_info_name_placeholder),
                icon = Icons.Default.Person,
                bgColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun ObservationsCard(
    comment: String,
    onCommentChange: (String) -> Unit
) {
    FiscalCard(title = stringResource(Res.string.home_fiscal_info_observations_title)) {
        CommentInput(
            modifier = Modifier.height(100.dp),
            value = comment,
            onValueChange = onCommentChange
        )
    }
}

@Composable
private fun FiscalCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}