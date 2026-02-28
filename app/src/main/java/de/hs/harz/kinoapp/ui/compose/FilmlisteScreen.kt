/**
 * Screen: Filmliste.
 *
 * Zeigt alle Filme als Karten an, bietet Suche, Hinzufügen (Dialog) und Löschen (mit Bestätigung).
 * Die Daten kommen aus dem MainViewModel – der Screen selbst hat keine direkte DB-Kenntnis.
 * Suche filtert nur die Anzeige, die Datenbank bleibt unverändert.
 */
package de.hs.harz.kinoapp.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.hs.harz.kinoapp.model.Film
import de.hs.harz.kinoapp.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmlisteScreen(viewModel: MainViewModel) {
    val filme = viewModel.filme
    var zeigDialog by remember { mutableStateOf(false) }      // AddFilm-Dialog sichtbar?
    var filmZumLoeschen by remember { mutableStateOf<Film?>(null) }  // Lösch-Bestätigung
    var suchtext by remember { mutableStateOf("") }

    // Filtern, wenn im Suchfeld was steht – Titel, Genre oder Beschreibung
    val gefilterteFilme = if (suchtext.isBlank()) {
        filme
    } else {
        filme.filter { film ->
            film.titel.contains(suchtext, ignoreCase = true) ||
            film.genre.contains(suchtext, ignoreCase = true) ||
            film.beschreibung.contains(suchtext, ignoreCase = true)
        }
    }

    // Wichtig: Der FAB liegt "oben" auf dem Inhalt (Overlay).
    // Sonst kann er durch ein `fillMaxSize()` der Liste aus dem sichtbaren Bereich gedrückt werden.
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = suchtext,
                onValueChange = { suchtext = it },
                label = { Text("Suchen") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Entweder leer oder kein Treffer bei der Suche
            if (gefilterteFilme.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (filme.isEmpty()) {
                        Text(
                            "Noch keine Filme. Tippe auf + zum Hinzufügen.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text("Keine Treffer.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                // LazyColumn = scrollbare Liste, rendert nur sichtbare Einträge (effizient)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(gefilterteFilme, key = { it.id }) { film ->
                        FilmKarte(film, onLoeschen = { filmZumLoeschen = film })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { zeigDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Film hinzufügen")
        }
    }

    // Dialog zum Film anlegen – öffnet sich über den FAB
    if (zeigDialog) {
        AddFilmDialog(
            onAbbrechen = { zeigDialog = false },
            onSpeichern = { neuerFilm ->
                // Speichern passiert über das ViewModel -> Repository -> DAO -> DB.
                viewModel.filmHinzufuegen(neuerFilm)
                zeigDialog = false
            }
        )
    }

    // Lösch-Dialog – verhindert versehentliches Entfernen
    if (filmZumLoeschen != null) {
        AlertDialog(
            onDismissRequest = { filmZumLoeschen = null },
            title = { Text("Film löschen") },
            text = { Text("\"${filmZumLoeschen!!.titel}\" wirklich löschen?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.filmLoeschen(filmZumLoeschen!!)
                    filmZumLoeschen = null
                }) {
                    Text("Löschen", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { filmZumLoeschen = null }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

// Einzelne Film-Karte – langer Druck oder Trash-Icon löschen
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun FilmKarte(film: Film, onLoeschen: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Langer Druck auf die ganze Karte = auch Löschen möglich
            .combinedClickable(
                onClick = { /* kein normaler Click nötig */ },
                onLongClick = onLoeschen
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail: entweder echtes Bild (falls URI gesetzt) oder Film-Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (film.bildUri != null) {
                    AsyncImage(
                        model = Uri.parse(film.bildUri),
                        contentDescription = "Filmbild",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(film.titel, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${film.datum} ${film.uhrzeit} · ${film.genre} · ${film.saal}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (film.beschreibung.isNotEmpty() && film.beschreibung != "-") {
                    Text(
                        film.beschreibung,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            }

            IconButton(onClick = onLoeschen) {
                Icon(
                    Icons.Default.Delete,
                    "Löschen",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
