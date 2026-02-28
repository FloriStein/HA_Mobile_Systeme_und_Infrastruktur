/**
 * Modul: Theme (KinoDesign)
 * Theme der App Material3
 *
 * Es wird bewusst nur ein dunkles Farbschema benutzt passt gut zur Kino-Optik
 * So bleibt die UI konsistent ohne dass man sich mit Light Dark Umstellung rumschlagen muss
 */
package de.hs.harz.kinoapp.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dunkles Kino-Farbschema Rot als Akzent dunkle Hintergründe
private val DunklesFarbschema = darkColorScheme(
    primary = Color(0xFFE94560),    // rot
    secondary = Color(0xFF1A1A2E),
    background = Color(0xFF0F0F1A),
    surface = Color(0xFF1A1A2E),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// Wrapper-Composable wendet Theme auf den gesamten Inhalt an
@Composable
fun KinoDesign(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DunklesFarbschema,
        typography = Typography(),
        content = content
    )
}
