package com.example.guitarscaleviewer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.IntervalModifier
import com.example.guitarscaleviewer.model.createScale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FretboardViewModel : ViewModel() {

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
        Log.d(",", "newKey:$newKey")
        update { it.copy(tonicNote = newKey, fretNotes = createScale(
            tonicNote = newKey,
            totalFrets = uiState.value.numFrets,
            intervals = setOf(
                Interval(1),
                Interval(2),
                Interval(3, IntervalModifier.FLAT),
                Interval(4),
                Interval(5),
                Interval(6, IntervalModifier.FLAT),
                Interval(7, IntervalModifier.FLAT),
            ) ))}
    }
}