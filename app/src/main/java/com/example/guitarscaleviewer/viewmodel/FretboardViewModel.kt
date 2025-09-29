package com.example.guitarscaleviewer.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.Scale
import com.example.guitarscaleviewer.model.allKeys
import com.example.guitarscaleviewer.model.createFretNotesScale
import com.example.guitarscaleviewer.model.loadScalesFromAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FretboardViewModel : ViewModel() {
    private val _scales = MutableStateFlow<List<Scale>>(emptyList())
    val scales: StateFlow<List<Scale>> = _scales

    val uiState = MutableStateFlow(
        FretboardUiState(
        )
    )

    init{
        val newNumFrets = 15
        val newTonicNote = "D"
        uiState.value = FretboardUiState(
            tonicNote = newTonicNote,
            numFrets = newNumFrets
        )
    }

    // update state by creating a new state
    private inline fun update(crossinline block: (FretboardUiState) -> FretboardUiState) {
        viewModelScope.launch {
            uiState.value = block(uiState.value)
        }
    }

    fun toggleShowScaleNum() = update { it.copy(showScaleNum = !it.showScaleNum) }

    fun updateKey(newKey: String)  {
        update { it.copy(tonicNote = newKey, fretNotes = createFretNotesScale(
            tonicNote = newKey,
            totalFrets = uiState.value.numFrets,
            intervals = setOf(
                Interval(1),
                Interval(2),
                Interval(3),
                Interval(4),
                Interval(5),
                Interval(6),
                Interval(7),
            )
        ))
        }
    }

    fun updateScale(newScale: Scale) {
        update { it.copy(scale = newScale, fretNotes = createFretNotesScale(
            tonicNote = uiState.value.tonicNote,
            totalFrets = uiState.value.numFrets,
            intervals = newScale.intervals
        )) }
    }

    fun randomizeScaleAndKey() {
        Log.d(",", "Random button pressed")
        val keys = allKeys
    }

    fun setScales(scales: List<Scale>) {
        _scales.value = scales
    }
}