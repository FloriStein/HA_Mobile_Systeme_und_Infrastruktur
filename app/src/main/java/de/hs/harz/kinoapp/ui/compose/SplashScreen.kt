/**
 * Einfacher Splash-Screen – Logo, Ladeindikator, 3 Sekunden Wartezeit.
 *
 * Nach Ablauf springt die App zur Filmliste (via onWeiter). Der Wechsel passiert
 * in der Navigation, hier nur der Timer mit LaunchedEffect.
 */
package de.hs.harz.kinoapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.hs.harz.kinoapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onWeiter: () -> Unit) {
    // LaunchedEffect läuft einmal beim ersten Anzeigen – dann 3 Sekunden warten
    LaunchedEffect(Unit) {
        delay(3000)
        onWeiter()
    }

    // Layout: Logo und Ladeanimation mittig auf dem Screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_hochschule_harz),
            contentDescription = "Hochschule Harz Logo",
            modifier = Modifier.size(200.dp, 100.dp)
        )

        Spacer(Modifier.height(40.dp))
        Text("Kino App wird geladen...", fontSize = 18.sp, color = Color.Black)
        Spacer(Modifier.height(20.dp))
        CircularProgressIndicator(color = Color.Blue, strokeWidth = 4.dp)
    }
}
