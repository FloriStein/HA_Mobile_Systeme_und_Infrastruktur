/**
 * Modul: MainViewModelFactory
 * Factory für das MainViewModel
 *
 * Compose viewModel erstellt ViewModels standardmäßig ohne Parameter wir brauchen
 * aber den Application-Context für die DB Diese Factory übergibt ihn beim Erzeugen
 */
package de.hs.harz.kinoapp.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Übergibt den Application-Context an viewModel in Compose
class MainViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Einfacher Cast hier wird nur MainViewModel erzeugt
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(app) as T
    }
}
