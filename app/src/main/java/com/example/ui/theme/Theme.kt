package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = SageGreenLight,
  onPrimary = ForestGreenDark,
  primaryContainer = SageGreenDark,
  onPrimaryContainer = SageGreenContainer,
  secondary = AccentCyan,
  onSecondary = Color.Black,
  tertiary = GoldPaidCoin,
  onTertiary = Color.Black,
  background = DarkBackground,
  onBackground = DarkOnSurface,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
  primary = SageGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = SageGreenContainer,
  onPrimaryContainer = OnSageGreenContainer,
  secondary = SageGreenDark,
  onSecondary = Color.White,
  tertiary = GoldPaidCoin,
  onTertiary = Color(0xFF3E2C00),
  background = LightBackground,
  onBackground = LightOnSurface,
  surface = LightSurface,
  onSurface = LightOnSurface,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightOnSurfaceVariant,
  outline = LightOutline
)

@Composable
fun MoodbookTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
