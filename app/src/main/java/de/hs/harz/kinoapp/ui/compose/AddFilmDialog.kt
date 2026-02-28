/**
 * Modul: AddFilmDialog
 * Dialog zum Anlegen eines neuen Films
 *
 * Sammelt alle Eingaben Datum Uhrzeit Titel Genre Saal optional Bild und
 * gibt am Ende ein Film-Objekt zurück Das Speichern passiert im FilmlisteScreen ViewModel
 * hier bleibt der Dialog dumm er liefert nur die Daten
 * Wichtig Content-URIs vom System-Picker werden beim Speichern in den internen
 * App-Speicher kopiert weil sie sonst nicht immer lesbar bleiben
 */
package de.hs.harz.kinoapp.ui.compose

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import de.hs.harz.kinoapp.model.Film
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFilmDialog(onAbbrechen: () -> Unit, onSpeichern: (Film) -> Unit) {
    val context = LocalContext.current

    // Lokale States für alle Formularfelder hier sammeln wir die Eingaben
    var datum by remember { mutableStateOf("") }
    var uhrzeit by remember { mutableStateOf("") }
    var zeigeDatePicker by remember { mutableStateOf(false) }
    var zeigeTimePicker by remember { mutableStateOf(false) }
    var titel by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        yearRange = IntRange(2020, 2030)
    )
    val timePickerState = rememberTimePickerState(
        initialHour = 20,
        initialMinute = 0
    )
    var beschreibung by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var saal by remember { mutableStateOf("") }
    var bildUri by remember { mutableStateOf<Uri?>(null) }
    var fehlerText by remember { mutableStateOf("") }

    val bildAuswahl = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> bildUri = uri }

    AlertDialog(
        onDismissRequest = onAbbrechen,
        title = { Text("Film hinzufügen") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = datum,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Datum (TT.MM.JJJJ)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Transparenter Klickbereich über dem Textfeld Tap öffnet den Picker
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                                zeigeDatePicker = true
                            }
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uhrzeit,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Uhrzeit (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                                zeigeTimePicker = true
                            }
                    )
                }
                OutlinedTextField(
                    value = titel, onValueChange = { titel = it },
                    label = { Text("Titel *") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = beschreibung,
                    onValueChange = { beschreibung = it },
                    label = { Text("Beschreibung") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = genre, onValueChange = { genre = it },
                    label = { Text("Genre") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = saal,
                    onValueChange = { saal = it },
                    label = { Text("Kinosaal") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Optional Bild aus Galerie wählen
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { bildAuswahl.launch("image/*") }) {
                        Text("Bild wählen")
                    }
                    if (bildUri != null) {
                        AsyncImage(
                            model = bildUri,
                            contentDescription = "Vorschau",
                            modifier = Modifier.size(56.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                if (fehlerText.isNotEmpty()) {
                    Text(fehlerText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Validierung Datum Uhrzeit und Titel müssen ausgefüllt sein
                if (datum.isBlank() || uhrzeit.isBlank() || titel.isBlank()) {
                    fehlerText = "Bitte Datum, Uhrzeit und Titel ausfüllen!"
                } else {
                    // Content-URIs vom Picker sind nicht immer dauerhaft lesbar daher kopieren wir
                    // das Bild in den internen App-Speicher und speichern diese URI
                    val gespeicherteBildUri = bildUri?.let { copyImageToInternalStorage(context, it) }

                    val neuerFilm = Film(
                        datum = datum.trim(),
                        uhrzeit = uhrzeit.trim(),
                        titel = titel.trim(),
                        beschreibung = if (beschreibung.isBlank()) "-" else beschreibung.trim(),
                        genre = if (genre.isBlank()) "-" else genre.trim(),
                        saal = if (saal.isBlank()) "-" else saal.trim(),
                        // URI als String speichern Room unterstützt keinen Uri-Typ
                        bildUri = gespeicherteBildUri
                    )
                    onSpeichern(neuerFilm)
                }
            }) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onAbbrechen) { Text("Abbrechen") }
        }
    )

    // DatePicker als separater Dialog wird über das Datum-Feld geöffnet
    if (zeigeDatePicker) {
        DatePickerDialog(
            onDismissRequest = { zeigeDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = Calendar.getInstance().apply { timeInMillis = millis }
                        datum = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(cal.time)
                    }
                    zeigeDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { zeigeDatePicker = false }) {
                    Text("Abbrechen")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker eigener kleiner Dialog mit TimeInput-Composable
    if (zeigeTimePicker) {
        Dialog(onDismissRequest = { zeigeTimePicker = false }) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp
            ) {
                Column(
                    Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Uhrzeit wählen", style = MaterialTheme.typography.headlineSmall)
                    TimeInput(state = timePickerState)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { zeigeTimePicker = false }) {
                            Text("Abbrechen")
                        }
                        TextButton(onClick = {
                            uhrzeit = "%02d:%02d".format(timePickerState.hour, timePickerState.minute)
                            zeigeTimePicker = false
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

// Kopiert Bild von Content-URI in App-interne Datei liefert file URI zurück oder null bei Fehler
private fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return runCatching {
        val resolver = context.contentResolver
        val mimeType = resolver.getType(uri).orEmpty().lowercase()
        val extension = when {
            mimeType.contains("png") -> ".png"
            mimeType.contains("webp") -> ".webp"
            else -> ".jpg"
        }

        val targetDir = File(context.filesDir, "film_images").apply { mkdirs() }
        val targetFile = File(targetDir, "film_${System.currentTimeMillis()}$extension")

        resolver.openInputStream(uri)?.use { input ->
            FileOutputStream(targetFile).use { output ->
                input.copyTo(output)
            }
        } ?: return null

        Uri.fromFile(targetFile).toString()
    }.getOrNull()
}
