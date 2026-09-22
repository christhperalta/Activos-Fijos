package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_product_section_header_empty_products_description
import activosfijos.composeapp.generated.resources.home_product_section_header_empty_products_title
import com.christhperalta.activosfijos.core.presentation.ProductItem
import com.christhperalta.activosfijos.core.quotation.Product
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductSection(
    quotationLines: List<Product>,
    distinctLineCount: Int,
    onQuantityChange: (String, Int) -> Unit,
    onDelete: (String) -> Unit,
    onScanner: () -> Unit,
    onManualSearch: () -> Unit,
    onSearchByName: () -> Unit,
) {

        Column(
            modifier = Modifier.padding(16.dp).heightIn(
                min = 300.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
//            HorizontalDivider(color = DividerColor, thickness = 2.dp)
            ProductSectionHeader(
                itemLineCount = distinctLineCount,
                onScanner = onScanner,
                onManualSearch = onManualSearch,
                onSearchByName = onSearchByName

            )
//            HorizontalDivider(color = DividerColor, thickness = 2.dp)

            if (quotationLines.isEmpty()) {
                EmptyProductsContent()
            } else {
                quotationLines.forEach { line ->
                    ProductItemRow(
                        line = line,
                        onQuantityChange = onQuantityChange,
                        onDelete = onDelete
                    )
                }
            }
        }
}
@Composable
fun EmptyProductsContent(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.home_product_section_header_empty_products_title),
    description: String = stringResource(Res.string.home_product_section_header_empty_products_description)
) {
    Column(
        modifier = modifier.padding(vertical = 30.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Rounded.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(54.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
@Composable
private fun ProductItemRow(
    line: Product,
    onQuantityChange: (String, Int) -> Unit,
    onDelete: (String) -> Unit
) {
    val itemCode = line.itemCode ?: return // si no tiene código, no renderiza

    ProductItem(
        name = line.itemDescription.orEmpty(),
        sku = itemCode,
        price = line.unitPrice ?: 0L,
        quantity = line.quantity ?: 1,
        itebisPerUnit = (line.unitPrice ?: 0L) * 18 / 100,
        onQuantityChange = { onQuantityChange(itemCode, it) },
        onDelete = { onDelete(itemCode) }
    )
}