package de.hs.harz.kinoapp.ui.compose

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.hs.harz.kinoapp.ui.main.MainViewModel
import de.hs.harz.kinoapp.ui.main.MainViewModelFactory

/**
 * Navigation und App-Shell in Compose.
 *
 * Hier wird festgelegt:
 * - welche Screens es gibt (Routes: startseite, filmliste, info)
 * - wie man zwischen ihnen navigiert (NavHost + BottomBar)
 * - wann TopBar/BottomBar sichtbar sind (nicht auf dem Splash)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeineKinoApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val aktuelleRoute = backStackEntry?.destination?.route

    // Scaffold = Grundgerüst mit TopBar, BottomBar und Platz für den Inhalt
    Scaffold(
        topBar = {
            // Auf dem Splash keine TopBar – sieht sonst doof aus
            if (aktuelleRoute != "startseite") {
                TopAppBar(title = { Text("Kino App") })
            }
        },
        bottomBar = {
            if (aktuelleRoute != "startseite") {
                NavigationBar {
                    NavigationBarItem(
                        selected = aktuelleRoute == "filmliste",
                        // Einfaches Navigieren – für diese kleine App reicht das
                        onClick = { navController.navigate("filmliste") },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Filme") }
                    )
                    NavigationBarItem(
                        selected = aktuelleRoute == "info",
                        onClick = { navController.navigate("info") },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        label = { Text("Infos") }
                    )
                }
            }
        }
    ) { padding ->
        // NavHost = Container für alle Screens, hier werden die Routes definiert
        NavHost(
            navController = navController,
            startDestination = "startseite",
            modifier = Modifier.padding(padding)
        ) {
            composable("startseite") {
                // Splash lädt ein paar Sekunden und springt dann zur Filmliste
                SplashScreen(
                    onWeiter = {
                        // inclusive = true damit man mit Back nicht zurück zum Splash kommt
                        navController.navigate("filmliste") {
                            popUpTo("startseite") { inclusive = true }
                        }
                    }
                )
            }
            composable("filmliste") {
                // ViewModel braucht Application-Context für die DB – daher die Factory
                val ctx = LocalContext.current
                val vm: MainViewModel = viewModel(
                    factory = MainViewModelFactory(ctx.applicationContext as Application)
                )
                FilmlisteScreen(vm)
            }
            composable("info") {
                ImpressumScreen()
            }
        }
    }
}
