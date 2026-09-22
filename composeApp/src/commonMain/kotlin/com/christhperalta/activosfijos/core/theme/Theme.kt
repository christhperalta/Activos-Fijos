package com.christhperalta.activosfijos.core.theme



import androidx.compose.material3.*
import androidx.compose.runtime.Composable

val LightColorScheme = lightColorScheme(
    primary            = BluePrimary,
    onPrimary          = White,
    primaryContainer   = BlueMuted,
    onPrimaryContainer = BlueDarker,
    secondary          = BlueMuted,
    onSecondary        = BluePrimary,
    background         = White,
    onBackground       = TextPrimary,
    surface            = BlueLight,
    onSurface          = TextPrimary,
    onSurfaceVariant   = TextSecondary,
    error              = ErrorColor,
    errorContainer     = ErrorContainer,
    onError            = White,
)
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}