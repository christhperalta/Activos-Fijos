package com.christhperalta.activosfijos.core.presentation


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation



data class CustomInputConfig(
    val label: String = "",
    val placeholder: String = "",
    val icon: ImageVector = Icons.Default.Visibility,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isPassword: Boolean = false,
    val isOnlyPassword : Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Next,
    val readOnly: Boolean = false,
    val bgColor: Color = Color(0x00000000),
    val keyboardOptions: KeyboardOptions? = null,
    val keyboardActions: KeyboardActions? = null,
    val trailingIcon: (@Composable () -> Unit)? = null,
    val maxLength: Int = Int.MAX_VALUE,
    val enabled: Boolean = true,
    val supportingText: String? = null,
)

@Composable
fun CustomInput(
    modifier: Modifier = Modifier,
    value: String?,
    config: CustomInputConfig = CustomInputConfig(),
    fieldAction: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val resolvedKeyboardOptions = config.keyboardOptions
        ?: KeyboardOptions(
            keyboardType = config.keyboardType,
            imeAction = config.imeAction
        )

    val resolvedKeyboardActions = config.keyboardActions
        ?: KeyboardActions(onAny = { fieldAction() })

    OutlinedTextField(
        value = value ?: "",
        onValueChange = { input ->
            if (input.length <= config.maxLength) onValueChange(input)
        },
        modifier = modifier.fillMaxWidth(),
        enabled = config.enabled,
        readOnly = config.readOnly,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        isError = config.isError,
        keyboardOptions = resolvedKeyboardOptions,
        keyboardActions = resolvedKeyboardActions,
        visualTransformation = if (config.isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,

        label = {
            Text(
                text = config.label,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        placeholder = {
            Text(
                text = config.placeholder,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        },

        leadingIcon = {
            if (config.isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    enabled = !config.isOnlyPassword
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                Icon(
                    imageVector = config.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },


        trailingIcon = config.trailingIcon,


        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedContainerColor = config.bgColor,
            focusedContainerColor = config.bgColor,
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        ),
    )
}