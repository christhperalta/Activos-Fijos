package com.christhperalta.activosfijos.feature.product.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.product_detail_add_to_cart
import activosfijos.composeapp.generated.resources.product_detail_not_found_title
import activosfijos.composeapp.generated.resources.product_detail_sku_label
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialog
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialogActions
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.core.presentation.ProductItem
import com.christhperalta.activosfijos.core.presentation.SessionExpiredAlertDialog
import com.christhperalta.activosfijos.core.quotation.Product
import com.christhperalta.activosfijos.core.quotation.QuotationCart
import com.christhperalta.activosfijos.core.utils.formatPrice
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ProductDetailScreen(
    quotationCart: QuotationCart = koinInject(),
    viewModel: ProductDetailViewModel = koinInject(),
    onBack: () -> Unit,
    onLogin: () -> Unit,
    productId: String?,
    productBarcode: String?,
) {

}


@Composable
private fun LoadingBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}


@Composable
private fun ProductDetailContent(
    modifier: Modifier = Modifier,
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductCard(
            product = product,
        )

        ProductItem(
            name = product.itemDescription ?: "",
            sku = "${stringResource(Res.string.product_detail_sku_label)} ${product.itemCode}",
            price = product.unitPrice ?: 0L,
            quantity = quantity,
            itebisPerUnit = (product.unitPrice ?: 0L) * 18 / 100,
            onQuantityChange = onQuantityChange,
            isCancellable = false
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxSize()
                .height(52.dp),
            shape = RoundedCornerShape(50),
        ) {
            Text(text = stringResource(Res.string.product_detail_add_to_cart))
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductImageBox(
            imageUrl = "",   // ← pasar dato real
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp, max = 480.dp)
        )
        ProductDetailsColumn(product = product)

    }
}




@Composable
fun ProductImageBox(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
//            AsyncImage(                              // ← ahora sí se usa
//                model = imageUrl,
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier.fillMaxSize()
//            )
        } else {
            MesaPlaceholder()
        }
    }
}

@Composable
private fun MesaPlaceholder(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(100.dp)
    ) {
        val w = size.width
        val h = size.height
        val tableColor = Color(0xFFA0703A)
        val darkWood = Color(0xFF7A4E2D)

        drawRoundRect(
            color = tableColor,
            topLeft = Offset(w * 0.05f, h * 0.32f),
            size = Size(w * 0.90f, h * 0.22f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        listOf(0.12f, 0.76f).forEach { x ->
            drawRoundRect(
                color = darkWood,
                topLeft = Offset(w * x, h * 0.54f),
                size = Size(w * 0.10f, h * 0.36f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

@Composable
private fun ProductDetailsColumn(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = product.itemDescription ?: "",
            fontWeight = FontWeight.Bold,
//            lineHeight = 22.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatPrice(product.unitPrice ?: 0L),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))

    }
}