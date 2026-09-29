package com.christhperalta.activosfijos.feature.product.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.product_detail_back_description
import activosfijos.composeapp.generated.resources.product_detail_branch_label
import activosfijos.composeapp.generated.resources.product_detail_department_label
import activosfijos.composeapp.generated.resources.product_detail_edit
import activosfijos.composeapp.generated.resources.product_detail_edit_title
import activosfijos.composeapp.generated.resources.product_detail_image_placeholder
import activosfijos.composeapp.generated.resources.product_detail_location_title
import activosfijos.composeapp.generated.resources.product_detail_office_label
import activosfijos.composeapp.generated.resources.product_detail_placeholder_branch
import activosfijos.composeapp.generated.resources.product_detail_placeholder_code
import activosfijos.composeapp.generated.resources.product_detail_placeholder_department
import activosfijos.composeapp.generated.resources.product_detail_placeholder_name
import activosfijos.composeapp.generated.resources.product_detail_placeholder_office
import activosfijos.composeapp.generated.resources.product_detail_placeholder_status
import activosfijos.composeapp.generated.resources.product_detail_placeholder_warehouse
import activosfijos.composeapp.generated.resources.product_detail_sku_label
import activosfijos.composeapp.generated.resources.product_detail_status_label
import activosfijos.composeapp.generated.resources.product_detail_status_active
import activosfijos.composeapp.generated.resources.product_detail_status_decommissioned
import activosfijos.composeapp.generated.resources.product_detail_status_inactive
import activosfijos.composeapp.generated.resources.product_detail_title
import activosfijos.composeapp.generated.resources.product_detail_warehouse_label
import activosfijos.composeapp.generated.resources.product_detail_cancel
import activosfijos.composeapp.generated.resources.product_detail_code_label
import activosfijos.composeapp.generated.resources.product_detail_name_label
import activosfijos.composeapp.generated.resources.product_detail_save
import com.christhperalta.activosfijos.core.quotation.Product
import com.christhperalta.activosfijos.core.theme.SuccessColor
import com.christhperalta.activosfijos.core.theme.SuccessContainer
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    productId: String?,
) {
    var product by remember { mutableStateOf(demoProduct) }
    var draftProduct by remember { mutableStateOf(demoProduct) }
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.product_detail_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(
                                Res.string.product_detail_back_description
                            )
                        )
                    }
                },
                actions = {
                    if (!isEditing) {
                        TextButton(
                            onClick = {
                                draftProduct = product
                                isEditing = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(Res.string.product_detail_edit),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        ProductDetailContent(
            modifier = Modifier.padding(innerPadding),
            product = product
        )
    }

    if (isEditing) {
        ModalBottomSheet(
            onDismissRequest = { isEditing = false },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            AssetEditForm(
                product = draftProduct,
                onProductChange = { draftProduct = it },
                onSave = {
                    product = draftProduct
                    isEditing = false
                },
                onCancel = { isEditing = false }
            )
        }
    }
}

private val demoProduct = Product(
    itemCode = "AF-000124",
    itemDescription = "Laptop Dell Latitude 5420",
    branch = "Santo Domingo",
    department = "Tecnología",
    office = "Soporte técnico",
    whsCode = "Almacén principal",
    assetStatus = "Activo"
)

@Composable
private fun ProductDetailContent(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProductImagePlaceholder()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = product.itemDescription.orPlaceholder(
                    Res.string.product_detail_placeholder_name
                ),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = stringResource(
                        Res.string.product_detail_sku_label,
                        product.itemCode.orPlaceholder(
                            Res.string.product_detail_placeholder_code
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        AssetLocationCard(product)
        AssetStatus(
            product.assetStatus.orPlaceholder(
                Res.string.product_detail_placeholder_status
            )
        )
    }
}

@Composable
private fun AssetEditForm(
    product: Product,
    onProductChange: (Product) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.product_detail_edit_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_name_label),
                value = product.itemDescription.orEmpty(),
                onValueChange = { onProductChange(product.copy(itemDescription = it)) }
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_code_label),
                value = product.itemCode.orEmpty(),
                onValueChange = { onProductChange(product.copy(itemCode = it)) }
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_branch_label),
                value = product.branch.orEmpty(),
                onValueChange = { onProductChange(product.copy(branch = it)) }
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_department_label),
                value = product.department.orEmpty(),
                onValueChange = { onProductChange(product.copy(department = it)) }
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_office_label),
                value = product.office.orEmpty(),
                onValueChange = { onProductChange(product.copy(office = it)) }
            )
            AssetTextField(
                label = stringResource(Res.string.product_detail_warehouse_label),
                value = product.whsCode.orEmpty(),
                onValueChange = { onProductChange(product.copy(whsCode = it)) }
            )
            AssetStatusSelector(
                status = product.assetStatus.orEmpty(),
                onStatusChange = { onProductChange(product.copy(assetStatus = it)) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text(stringResource(Res.string.product_detail_cancel))
                }
                Button(onClick = onSave) {
                    Text(stringResource(Res.string.product_detail_save))
                }
            }
        }
    }
}

@Composable
private fun AssetTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetStatusSelector(
    status: String,
    onStatusChange: (String) -> Unit,
) {
    val options = listOf(
        stringResource(Res.string.product_detail_status_active),
        stringResource(Res.string.product_detail_status_inactive),
        stringResource(Res.string.product_detail_status_decommissioned)
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = status,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            label = { Text(stringResource(Res.string.product_detail_status_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onStatusChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                MaterialTheme.colorScheme.background
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Image,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.product_detail_image_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AssetLocationCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(Res.string.product_detail_location_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            AssetDetailRow(
                icon = Icons.Rounded.Storefront,
                label = stringResource(Res.string.product_detail_branch_label),
                value = product.branch.orPlaceholder(
                    Res.string.product_detail_placeholder_branch
                )
            )
            AssetDetailRow(
                icon = Icons.Rounded.AccountTree,
                label = stringResource(Res.string.product_detail_department_label),
                value = product.department.orPlaceholder(
                    Res.string.product_detail_placeholder_department
                )
            )
            AssetDetailRow(
                icon = Icons.Rounded.Business,
                label = stringResource(Res.string.product_detail_office_label),
                value = product.office.orPlaceholder(
                    Res.string.product_detail_placeholder_office
                )
            )
            AssetDetailRow(
                icon = Icons.Rounded.Warehouse,
                label = stringResource(Res.string.product_detail_warehouse_label),
                value = product.whsCode.orPlaceholder(
                    Res.string.product_detail_placeholder_warehouse
                ),
                showDivider = false
            )
        }
    }
}

@Composable
private fun AssetDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun AssetStatus(status: String) {
    val activeLabel = stringResource(Res.string.product_detail_status_active)
    val decommissionedLabel = stringResource(Res.string.product_detail_status_decommissioned)

    val (container, content) = when (status) {
        activeLabel -> SuccessContainer to SuccessColor
        decommissionedLabel ->
            MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error

        else -> MaterialTheme.colorScheme.surfaceContainerHigh to
                MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.product_detail_status_label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            shape = RoundedCornerShape(50),
            color = container,
            contentColor = content
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(content)
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun String?.orPlaceholder(
    placeholder: org.jetbrains.compose.resources.StringResource,
): String =
    this?.takeIf { it.isNotBlank() }
        ?: stringResource(placeholder)
