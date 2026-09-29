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
    background         = BlueLight,
    onBackground       = TextPrimary,
    surface            = BlueLight,
    onSurface          = TextPrimary,
    onSurfaceVariant   = TextSecondary,
    error              = ErrorColor,
    errorContainer     = ErrorContainer,
    onError            = White,
    surfaceContainerLowest = White,
    surfaceContainerLow    = SurfaceLow,
    surfaceContainer       = SurfaceContainer,
    surfaceContainerHigh   = SurfaceHigh,
    surfaceContainerHighest = SurfaceHighest,
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
