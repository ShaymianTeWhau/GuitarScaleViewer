package com.example.guitarscaleviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitarscaleviewer.model.FretNote
import com.example.guitarscaleviewer.model.Instrument
import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.Scale
import com.example.guitarscaleviewer.model.allKeys
import com.example.guitarscaleviewer.model.createFretNotesScale
import com.example.guitarscaleviewer.model.getTuningForInstrument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FretboardViewModel : ViewModel() {
    private val _scales = MutableStateFlow<List<Scale>>(emptyList())
    private val scales: StateFlow<List<Scale>> = _scales

    val uiState = MutableStateFlow(
        FretboardUiState()
    )

    init{
        val newNumFrets = 15
        val newTonicNote = "C"
        uiState.value = FretboardUiState(
            tonicNote = newTonicNote,
            numFrets = newNumFrets,
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
        val newFretNotes: Set<FretNote>
        if(uiState.value.scale.name == "None"){
            newFretNotes = emptySet()
        }else{
            newFretNotes = createFretNotesScale(
                tonicNote = newKey,
                stringTuning = uiState.value.tuning,
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
            )
        }
        update { it.copy(tonicNote = newKey, fretNotes = newFretNotes)
        }
    }

    fun updateScale(newScale: Scale) {
        update { it.copy(
            scale = newScale,
            fretNotes = createFretNotesScale(
                tonicNote = uiState.value.tonicNote,
                stringTuning = uiState.value.tuning,
                totalFrets = uiState.value.numFrets,
                intervals = newScale.intervals
            )
        ) }
    }

    fun randomizeScaleAndKey() {
        val newKey = allKeys.random()
        val newScale = scales.value.random()

        update { it.copy(
            scale = newScale,
            tonicNote = newKey,
            fretNotes = createFretNotesScale(
                totalFrets = uiState.value.numFrets,
                stringTuning = uiState.value.tuning,
                tonicNote = newKey,
                intervals = newScale.intervals
            )
        ) }
    }

    fun setScales(scales: List<Scale>) {
        _scales.value = scales
    }

    fun updateStringCount(newStringCount: Int) {
        val newTuning  = getTuningForInstrument(uiState.value.instrument, newStringCount)
        uiState.value.tuning = newTuning
        update { it.copy(
            numStrings = newStringCount,
            fretNotes = createFretNotesScale(
                totalFrets = uiState.value.numFrets,
                stringTuning = newTuning,
                tonicNote = uiState.value.tonicNote,
                intervals = uiState.value.scale.intervals
            )
        ) }
    }

    fun updateFretCount(fretCount: Int) {
        update { it.copy(
            numFrets = fretCount,
            fretNotes = createFretNotesScale(
                totalFrets = fretCount,
                stringTuning = uiState.value.tuning,
                tonicNote = uiState.value.tonicNote,
                intervals = uiState.value.scale.intervals
            )
        ) }
    }

    fun updateInstrument(instrumentStr: String) {
        var newInstrument: Instrument
        var newStringCount: Int
        var newTuning: List<String>

        if(instrumentStr == "Guitar"){
            newInstrument = Instrument.GUITAR
            newStringCount = 6
        }else{
            newInstrument = Instrument.BASS
            newStringCount = 4
        }
        newTuning = getTuningForInstrument(newInstrument, newStringCount)

        update { it.copy(
            instrument = newInstrument,
            tuning = newTuning,
            numStrings = newStringCount,
            fretNotes = createFretNotesScale(
                totalFrets = uiState.value.numFrets,
                stringTuning = newTuning,
                tonicNote = uiState.value.tonicNote,
                intervals = uiState.value.scale.intervals
            )
            ) }
    }
}