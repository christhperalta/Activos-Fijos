package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_quotation_card_date
import activosfijos.composeapp.generated.resources.home_quotation_card_document_number
import activosfijos.composeapp.generated.resources.home_quotation_card_seller
import activosfijos.composeapp.generated.resources.home_quotation_card_subtotal
import activosfijos.composeapp.generated.resources.home_quotation_itebis_title
import com.christhperalta.activosfijos.core.presentation.AdaptiveLayout
import com.christhperalta.activosfijos.core.presentation.LayoutType
import com.christhperalta.activosfijos.core.utils.formatPrice
import com.christhperalta.activosfijos.core.utils.getCurrentDate
import org.jetbrains.compose.resources.stringResource


@Composable
fun QuotationCard(
    modifier: Modifier = Modifier,
    subTotal: Long = 0L,
    tax: Long = 0L,
    total: Long = 0L,
    sellerName: String = "",
    onQuotation: () -> Unit,
) {

        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CotizacionSectionHeader(
                onQuotation = onQuotation,
            )
//            HorizontalDivider(color = DividerColor, thickness = 2.dp)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                AdaptiveLayout { layoutType ->
                    when (layoutType) {
                        LayoutType.MOBILE_PORTRAIT -> {
                            MobilePortraitQuotationCard(
                                subTotal = subTotal,
                                sellerName = sellerName,
                                total = total,
                                tax = tax
                            )
                        }

                        LayoutType.TABLET_PORTRAIT -> {
                            PortraitQuotationCard(
                                subTotal = subTotal,
                                sellerName = sellerName,
                                total = total,
                                tax = tax
                            )
                        }

                        LayoutType.LANDSCAPE -> {
                            LandscapeQuotationCard(
                                subTotal = subTotal,
                                sellerName = sellerName,
                                total = total,
                                tax = tax
                            )
                        }
                    }
                }

            }
        }
}

@Composable
private fun MobilePortraitQuotationCard(
    sellerName: String,
    subTotal: Long,
    tax: Long,
    total: Long
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_document_number)){
                CustomInfoText(value = "132151", color = MaterialTheme.colorScheme.error)
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_date)){
                CustomInfoText(getCurrentDate())
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_seller)){
                CustomInfoText(sellerName)
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_subtotal), alignEnd = false){
                CustomInfoText(value = formatPrice(subTotal))
            }
            InfoItem(label = stringResource(Res.string.home_quotation_itebis_title), alignEnd = false){
                CustomInfoText(value =  formatPrice(tax))
            }
            Spacer(modifier = Modifier.height(4.dp))
            TotalBox(total = formatPrice(total))
        }
    }
}


@Composable
private fun PortraitQuotationCard(
    sellerName: String,
    subTotal: Long,
    tax: Long,
    total: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_document_number)){
                CustomInfoText(value = "132151", color = MaterialTheme.colorScheme.error)
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_date)){
                CustomInfoText(getCurrentDate())
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_seller)){
                CustomInfoText(sellerName)
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_subtotal), alignEnd = false){
                CustomInfoText(value = formatPrice(subTotal))
            }
            InfoItem(label = stringResource(Res.string.home_quotation_itebis_title), alignEnd = false){
                CustomInfoText(value =  formatPrice(tax))
            }
            Spacer(modifier = Modifier.height(4.dp))
            TotalBox(total = formatPrice(total))
        }
    }
}

@Composable
private fun LandscapeQuotationCard(
    sellerName: String,
    subTotal: Long,
    tax: Long,
    total: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_document_number)){
                CustomInfoText(value = "132151", color = MaterialTheme.colorScheme.error)
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_date)){
                CustomInfoText(getCurrentDate())
            }
            InfoItem(label = stringResource(Res.string.home_quotation_card_seller)){
                CustomInfoText(sellerName)
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoItem(label = stringResource(Res.string.home_quotation_card_subtotal), alignEnd = false){
                CustomInfoText(value = formatPrice(subTotal))
            }
            InfoItem(label = stringResource(Res.string.home_quotation_itebis_title), alignEnd = false){
                CustomInfoText(value =  formatPrice(tax))
            }
            Spacer(modifier = Modifier.height(4.dp))
            TotalBox(total = formatPrice(total))
        }
    }
}


@Composable
private fun CustomInfoText(
    value: String,
    color: Color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
){
    Text(
        text = value,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = color
    )
}