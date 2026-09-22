package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.product_item_image_placeholder
import activosfijos.composeapp.generated.resources.product_item_itebis_label
import activosfijos.composeapp.generated.resources.product_item_subtotal_label
import com.christhperalta.activosfijos.core.utils.formatPrice
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductItem(
    name: String,
    sku: String,
    price: Long,
    quantity: Int,
    itebisPerUnit: Long = 0L,
    isCancellable: Boolean = true,
    onQuantityChange: (Int) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val subtotal = price * quantity
    val totalItebis = itebisPerUnit * quantity

    val onDecrement = {
        val next = quantity - 1
        if (next >= 1) onQuantityChange(next)
    }
    val onIncrement = { onQuantityChange(quantity + 1) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        AdaptiveLayout { layoutType ->
            when (layoutType) {
                LayoutType.MOBILE_PORTRAIT -> {
                    NarrowLayout(
                        name = name,
                        sku = sku,
                        price = price,
                        quantity = quantity,
                        totalItebis = totalItebis,
                        subtotal = subtotal,
                        isCancellable = isCancellable,
                        onDelete = onDelete,
                        onDecrement = onDecrement,
                        onIncrement = onIncrement
                    )
                }
                LayoutType.TABLET_PORTRAIT, LayoutType.LANDSCAPE -> {
                    WideLayout(

                        name = name,
                        sku = sku,
                        price = price,
                        quantity = quantity,
                        totalItebis = totalItebis,
                        subtotal = subtotal,
                        isCancellable = isCancellable,
                        onDelete = onDelete,
                        onDecrement = onDecrement,
                        onIncrement = onIncrement
                    )
                }

            }
        }
    }}

@Composable
 fun NarrowLayout(
    name: String,
    sku: String,
    price: Long,
    quantity: Int,
    totalItebis: Long,
    subtotal: Long,
    isCancellable: Boolean,
    onDelete: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductItemThumb()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = sku,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Text(
                    text = formatPrice(price),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (isCancellable) DeleteButton(onClick = onDelete)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // Fila inferior: itebis + subtotal + quantity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriceLabel(label = stringResource(Res.string.product_item_itebis_label), value = formatPrice(totalItebis))
            PriceLabel(label = stringResource(Res.string.product_item_subtotal_label), value = formatPrice(subtotal), alignEnd = true)
            QuantityControl(
                quantity = quantity,
                onDecrement = onDecrement,
                onIncrement = onIncrement
            )
        }
    }
}

@Composable
 fun WideLayout(
    name: String,
    sku: String,
    price: Long,
    quantity: Int,
    totalItebis: Long,
    subtotal: Long,
    isCancellable: Boolean,
    onDelete: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) { 
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ProductItemThumb()

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sku,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Text(
                text = formatPrice(price),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }

        PriceLabel(label = stringResource(Res.string.product_item_itebis_label), value = formatPrice(totalItebis))
        PriceLabel(label = stringResource(Res.string.product_item_subtotal_label), value = formatPrice(subtotal))

        QuantityControl(
            quantity = quantity,
            onDecrement = onDecrement,
            onIncrement = onIncrement
        )

        if (isCancellable) DeleteButton(onClick = onDelete)
    }
}

@Composable
fun PriceLabel(
    label: String,
    value: String,
    alignEnd: Boolean = false
) {
    Column(horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
 fun ProductItemThumb() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.product_item_image_placeholder),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}