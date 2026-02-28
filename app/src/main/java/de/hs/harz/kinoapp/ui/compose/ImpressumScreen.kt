/**
 * Modul: ImpressumScreen
 * Info Impressum-Screen nur statischer Text
 *
 * Zeigt Impressumsinfos Urheber Matrikelnummer etc Kein ViewModel nötig
 * weil keine dynamischen Daten einfache Composable reicht aus
 */
package de.hs.harz.kinoapp.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImpressumScreen() {
    // Scrollbarer Textblock verticalScroll für lange Impressumstexte
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Impressum", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("Kino App")
        Text("Entwurfsarbeit Mobile Applikationen")
        Spacer(Modifier.height(8.dp))
        Text("Hochschule Harz")
        Spacer(Modifier.height(16.dp))
        Text("Urheber: Florian Steinmann")
        Text("Matrikelnummer: m29694")
        Text("U-Nummer: u37123")
        Text("Studiengang: Informatik")
        Text("Fachbereich: AI")
        Spacer(Modifier.height(32.dp))
        Text("2026", fontSize = 12.sp)
    }
}
