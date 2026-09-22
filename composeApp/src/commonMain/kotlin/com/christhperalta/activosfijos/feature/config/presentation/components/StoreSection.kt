package com.christhperalta.activosfijos.feature.config.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component3
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.config_btn_save
import activosfijos.composeapp.generated.resources.config_btn_saved
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_customer
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_customer_defauld
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_price_list
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_price_list_defauld
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_seller
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_seller_defauld
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_warehouse
import activosfijos.composeapp.generated.resources.config_store_section_dropdown_fild_warehouse_defauld
import activosfijos.composeapp.generated.resources.config_store_section_input_card_code
import activosfijos.composeapp.generated.resources.config_store_section_input_card_name
import activosfijos.composeapp.generated.resources.config_store_section_input_currency
import activosfijos.composeapp.generated.resources.config_store_section_input_rnc
import activosfijos.composeapp.generated.resources.config_store_section_input_tax_code
import activosfijos.composeapp.generated.resources.config_store_section_input_terminal_number
import activosfijos.composeapp.generated.resources.config_store_section_title
import com.christhperalta.activosfijos.core.presentation.AdaptiveLayout
import com.christhperalta.activosfijos.core.presentation.CustomDropdownField
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.core.presentation.LayoutType
import com.christhperalta.activosfijos.feature.config.presentation.ConfigUiState
import org.jetbrains.compose.resources.stringResource


data class StoreSectionCallbacks(
    val onSellerChange: (String, Int) -> Unit = { _, _ -> },
    val onWarehouseChange: (String, String) -> Unit = { _, _ -> },
    val onCustomerChange: (String, String) -> Unit = { _, _ -> },
    val onSave: () -> Unit = {},
    val onCardNameChange: (String) -> Unit = {},
    val onDocCurrencyChange: (String) -> Unit = {},
    val onTaxCodeChange: (String) -> Unit = {},
    val onTerminalNumberChange: (String) -> Unit = {},
    val onCardCodeChange: (String) -> Unit = {},
    val onPriceListChange: (String, String) -> Unit = { _, _ -> },
    val onRnc: (String) -> Unit = {},
)

@Composable
fun StoreSection(
    state: ConfigUiState,
    callbacks: StoreSectionCallbacks = StoreSectionCallbacks(),
) {
    ConfigSectionCard(
        title = stringResource(Res.string.config_store_section_title),
        icon = Icons.Default.Store,
    ) {


        AdaptiveLayout { layoutType ->
            when (layoutType) {
                LayoutType.MOBILE_PORTRAIT -> {
                    MobilePortraitConfig(
                        state = state,
                        callbacks = callbacks
                    )
                }

                LayoutType.TABLET_PORTRAIT -> {
                    PortraitConfig(
                        state = state,
                        callbacks = callbacks
                    )
                }

                LayoutType.LANDSCAPE -> {
                    LandscapeConfig(
                        state = state,
                        callbacks = callbacks
                    )
                }
            }
        }

        CustomPrimaryButton(
            modifier = Modifier.align(Alignment.End).height(46.dp).padding(horizontal = 0.dp)
                .fillMaxWidth(0.5f),
            enabled = state.sellerName?.isNotBlank() == true && state.taxCode?.isNotBlank() == true,
            onClick = callbacks.onSave
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            CustomText(
                text = if (state.isStoreSaved) stringResource(Res.string.config_btn_saved) else stringResource(
                    Res.string.config_btn_save
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}


@Composable
private fun PortraitConfig(
    state: ConfigUiState,
    callbacks: StoreSectionCallbacks = StoreSectionCallbacks(),
) {

    val (focus1, focus2, focus3, focus4, focus5, focus6) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomDropdownField(
                icon = Icons.Default.Person,
                label = stringResource(Res.string.config_store_section_dropdown_fild_seller),
                selected = state.sellerList.find { it.slpCode == state.sellerCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_seller_defauld),
                options = state.sellerList,
                itemLabel = { "${it.slpCode} - ${it.slpName}" },
                onSelect = { seller ->
                    callbacks.onSellerChange(seller.slpName ?: "", seller.slpCode ?: 0)
                }
            )


            CustomDropdownField(
                icon = Icons.Default.Warehouse,
                label = stringResource(Res.string.config_store_section_dropdown_fild_warehouse),
                selected = state.warehouseList.find { it.warehouseCode == state.warehouseCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_warehouse_defauld),
                options = state.warehouseList,
                onSelect = {
                    callbacks.onWarehouseChange(it.warehouseName ?: "", it.warehouseCode ?: "")
                },
                itemLabel = { "${it.warehouseCode} - ${it.warehouseName}" }
            )


            CustomDropdownField(
                icon = Icons.Default.Person,
                label = stringResource(Res.string.config_store_section_dropdown_fild_customer),
                selected = state.customerList.find { it.customerCode == state.customerCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_customer_defauld),
                options = state.customerList,
                onSelect = {
                    callbacks.onCustomerChange(it.customerName ?: "", it.customerCode ?: "")
                },
                itemLabel = { "${it.customerCode} - ${it.customerName}" }
            )

            CustomDropdownField(
                icon = Icons.Default.FilterList,
                label = stringResource(Res.string.config_store_section_dropdown_fild_price_list),
                selected = state.priceList.find { it.priceListCode == state.priceListCode.toString() },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_price_list_defauld),
                options = state.priceList,
                onSelect = {
                    callbacks.onPriceListChange(it.priceListName, it.priceListCode)
                },
                itemLabel = { "${it.priceListCode} - ${it.priceListName}" }

            )


            CustomInput(
                modifier = Modifier.focusRequester(focus1),
                value = state.terminalNumber ?: "",
                onValueChange = callbacks.onTerminalNumberChange,
                fieldAction = { focus2.requestFocus() },

                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_terminal_number),
                    icon = Icons.Default.PointOfSale,
                    isError = state.terminalNumber?.isBlank() == true && state.isStoreSaved,
                ),
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomInput(
                modifier = Modifier.focusRequester(focus2),
                value = state.rnc ?: "",
                onValueChange = callbacks.onRnc,
                fieldAction = { focus3.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_rnc),
                    icon = Icons.Default.Badge,
                    isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
                ),
            )


            CustomInput(
                modifier = Modifier.focusRequester(focus3),
                value = state.cardCode ?: "",
                onValueChange = callbacks.onCardCodeChange,
                fieldAction = { focus4.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_card_code),
                    icon = Icons.Default.Code,
                    isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
                ),
            )




            CustomInput(
                modifier = Modifier.focusRequester(focus4),
                value = state.cardName ?: "",
                onValueChange = callbacks.onCardNameChange,
                fieldAction = { focus5.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_card_name),
                    icon = Icons.Default.PointOfSale,
                    isError = state.cardName?.isBlank() == true && state.isStoreSaved,
                ),
            )

            CustomInput(
                modifier = Modifier.focusRequester(focus5),
                value = state.docCurrency ?: "",
                onValueChange = callbacks.onDocCurrencyChange,
                fieldAction = { focus6.requestFocus() },

                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_currency),
                    icon = Icons.Default.CurrencyExchange,
                    isError = state.docCurrency?.isBlank() == true && state.isStoreSaved,
                ),
            )


            CustomInput(
                modifier = Modifier.focusRequester(focus6),
                value = state.taxCode ?: "",
                onValueChange = callbacks.onTaxCodeChange,
                fieldAction = { keyboardController?.hide() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_tax_code),
                    icon = Icons.Default.AccountBalance,
                    isError = state.taxCode?.isBlank() == true && state.isStoreSaved,
                    imeAction = ImeAction.Done
                ),
            )

        }

    }
}


@Composable
private fun LandscapeConfig(
    modifier: Modifier = Modifier,
    state: ConfigUiState,
    callbacks: StoreSectionCallbacks = StoreSectionCallbacks(),
) {

    val (focus1, focus2, focus3, focus4, focus5, focus6) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row {

        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomDropdownField(
                icon = Icons.Default.Person,
                label = stringResource(Res.string.config_store_section_dropdown_fild_seller),
                selected = state.sellerList.find { it.slpCode == state.sellerCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_seller_defauld),
                options = state.sellerList,
                itemLabel = { "${it.slpCode} - ${it.slpName}" },
                onSelect = { seller ->
                    callbacks.onSellerChange(seller.slpName ?: "", seller.slpCode ?: 0)
                }
            )


            CustomDropdownField(
                icon = Icons.Default.Warehouse,
                label = stringResource(Res.string.config_store_section_dropdown_fild_warehouse),
                selected = state.warehouseList.find { it.warehouseCode == state.warehouseCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_warehouse_defauld),
                options = state.warehouseList,
                onSelect = {
                    callbacks.onWarehouseChange(it.warehouseName ?: "", it.warehouseCode ?: "")
                },
                itemLabel = { "${it.warehouseCode} - ${it.warehouseName}" }
            )


            CustomDropdownField(
                icon = Icons.Default.Person,
                label = stringResource(Res.string.config_store_section_dropdown_fild_customer),
                selected = state.customerList.find { it.customerCode == state.customerCode },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_customer_defauld),
                options = state.customerList,
                onSelect = {
                    callbacks.onCustomerChange(it.customerName ?: "", it.customerCode ?: "")
                },
                itemLabel = { "${it.customerCode} - ${it.customerName}" }
            )

            CustomDropdownField(
                icon = Icons.Default.FilterList,
                label = stringResource(Res.string.config_store_section_dropdown_fild_price_list),
                selected = state.priceList.find { it.priceListCode == state.priceListCode.toString() },
                defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_price_list_defauld),
                options = state.priceList,
                onSelect = {
                    callbacks.onPriceListChange(it.priceListName, it.priceListCode)
                },
                itemLabel = { "${it.priceListCode} - ${it.priceListName}" }

            )


            CustomInput(
                modifier = Modifier.focusRequester(focus1),
                value = state.terminalNumber ?: "",
                onValueChange = callbacks.onTerminalNumberChange,
                fieldAction = { focus2.requestFocus() },

                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_terminal_number),
                    icon = Icons.Default.PointOfSale,
                    isError = state.terminalNumber?.isBlank() == true && state.isStoreSaved,
                ),
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomInput(
                modifier = Modifier.focusRequester(focus2),
                value = state.rnc ?: "",
                onValueChange = callbacks.onRnc,
                fieldAction = { focus3.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_rnc),
                    icon = Icons.Default.Badge,
                    isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
                ),
            )


            CustomInput(
                modifier = Modifier.focusRequester(focus3),
                value = state.cardCode ?: "",
                onValueChange = callbacks.onCardCodeChange,
                fieldAction = { focus4.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_card_code),
                    icon = Icons.Default.Code,
                    isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
                ),
            )




            CustomInput(
                modifier = Modifier.focusRequester(focus4),
                value = state.cardName ?: "",
                onValueChange = callbacks.onCardNameChange,
                fieldAction = { focus5.requestFocus() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_card_name),
                    icon = Icons.Default.PointOfSale,
                    isError = state.cardName?.isBlank() == true && state.isStoreSaved,
                ),
            )

            CustomInput(
                modifier = Modifier.focusRequester(focus5),
                value = state.docCurrency ?: "",
                onValueChange = callbacks.onDocCurrencyChange,
                fieldAction = { focus6.requestFocus() },

                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_currency),
                    icon = Icons.Default.CurrencyExchange,
                    isError = state.docCurrency?.isBlank() == true && state.isStoreSaved,
                ),
            )


            CustomInput(
                modifier = Modifier.focusRequester(focus6),
                value = state.taxCode ?: "",
                onValueChange = callbacks.onTaxCodeChange,
                fieldAction = { keyboardController?.hide() },
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_store_section_input_tax_code),
                    icon = Icons.Default.AccountBalance,
                    isError = state.taxCode?.isBlank() == true && state.isStoreSaved,
                    imeAction = ImeAction.Done
                ),
            )

        }

    }
}


@Composable
private fun MobilePortraitConfig(
    state: ConfigUiState,
    callbacks: StoreSectionCallbacks,
) {

    val (focus1, focus2, focus3, focus4, focus5, focus6) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomDropdownField(
            icon = Icons.Default.Person,
            label = stringResource(Res.string.config_store_section_dropdown_fild_seller),
            selected = state.sellerList.find { it.slpCode == state.sellerCode },
            defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_seller_defauld),
            options = state.sellerList,
            itemLabel = { "${it.slpCode} - ${it.slpName}" },
            onSelect = { seller ->
                callbacks.onSellerChange(seller.slpName ?: "", seller.slpCode ?: 0)
            }
        )

        CustomDropdownField(
            icon = Icons.Default.Warehouse,
            label = stringResource(Res.string.config_store_section_dropdown_fild_warehouse),
            selected = state.warehouseList.find { it.warehouseCode == state.warehouseCode },
            defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_warehouse_defauld),
            options = state.warehouseList,
            onSelect = {
                callbacks.onWarehouseChange(it.warehouseName ?: "", it.warehouseCode ?: "")
            },
            itemLabel = { "${it.warehouseCode} - ${it.warehouseName}" }
        )

        CustomDropdownField(
            icon = Icons.Default.Person,
            label = stringResource(Res.string.config_store_section_dropdown_fild_customer),
            selected = state.customerList.find { it.customerCode == state.customerCode },
            defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_customer_defauld),
            options = state.customerList,
            onSelect = {
                callbacks.onCustomerChange(it.customerName ?: "", it.customerCode ?: "")
            },
            itemLabel = { "${it.customerCode} - ${it.customerName}" }
        )

        CustomDropdownField(
            icon = Icons.Default.FilterList,
            label = stringResource(Res.string.config_store_section_dropdown_fild_price_list),
            selected = state.priceList.find { it.priceListCode == state.priceListCode.toString() },
            defaultValue = stringResource(Res.string.config_store_section_dropdown_fild_price_list_defauld),
            options = state.priceList,
            onSelect = {
                callbacks.onPriceListChange(it.priceListName, it.priceListCode)
            },
            itemLabel = { "${it.priceListCode} - ${it.priceListName}" }

        )

        CustomInput(
            modifier = Modifier.focusRequester(focus1),
            value = state.terminalNumber ?: "",
            onValueChange = callbacks.onTerminalNumberChange,
            fieldAction = { focus2.requestFocus() },

            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_terminal_number),
                icon = Icons.Default.PointOfSale,
                isError = state.terminalNumber?.isBlank() == true && state.isStoreSaved,
            ),
        )

        CustomInput(
            modifier = Modifier.focusRequester(focus2),
            value = state.rnc ?: "",
            onValueChange = callbacks.onRnc,
            fieldAction = { focus3.requestFocus() },
            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_rnc),
                icon = Icons.Default.Badge,
                isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
            ),
        )

        CustomInput(
            modifier = Modifier.focusRequester(focus3),
            value = state.cardCode ?: "",
            onValueChange = callbacks.onCardCodeChange,
            fieldAction = { focus4.requestFocus() },
            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_card_code),
                icon = Icons.Default.Code,
                isError = state.cardCode?.isBlank() == true && state.isStoreSaved,
            ),
        )

        CustomInput(
            modifier = Modifier.focusRequester(focus4),
            value = state.cardName ?: "",
            onValueChange = callbacks.onCardNameChange,
            fieldAction = { focus5.requestFocus() },
            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_card_name),
                icon = Icons.Default.PointOfSale,
                isError = state.cardName?.isBlank() == true && state.isStoreSaved,
            ),
        )

        CustomInput(
            modifier = Modifier.focusRequester(focus5),
            value = state.docCurrency ?: "",
            onValueChange = callbacks.onDocCurrencyChange,
            fieldAction = { focus6.requestFocus() },

            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_currency),
                icon = Icons.Default.CurrencyExchange,
                isError = state.docCurrency?.isBlank() == true && state.isStoreSaved,
            ),
        )


        CustomInput(
            modifier = Modifier.focusRequester(focus6),
            value = state.taxCode ?: "",
            onValueChange = callbacks.onTaxCodeChange,
            fieldAction = { keyboardController?.hide() },
            config = CustomInputConfig(
                label = stringResource(Res.string.config_store_section_input_tax_code),
                icon = Icons.Default.AccountBalance,
                isError = state.taxCode?.isBlank() == true && state.isStoreSaved,
                imeAction = ImeAction.Done
            ),
        )


    }

}
