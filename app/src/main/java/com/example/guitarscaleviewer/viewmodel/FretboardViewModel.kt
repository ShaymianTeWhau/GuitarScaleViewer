package com.example.guitarscaleviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.IntervalModifier
import com.example.guitarscaleviewer.model.createScale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FretboardViewModel : ViewModel() {

    val exampleScale = createScale(
        intervals = setOf(
            Interval(1),
            Interval(2),
            Interval(3, IntervalModifier.FLAT),
            Interval(4),
            Interval(5),
            Interval(6, IntervalModifier.FLAT),
            Interval(7, IntervalModifier.FLAT),
        )
    )
    val uiState = MutableStateFlow(
        FretboardUiState(
            fretNotes = exampleScale
        )
    )

    // update state by creating a new state
    private inline fun update(crossinline block: (FretboardUiState) -> FretboardUiState) {
        viewModelScope.launch {
            uiState.value = block(uiState.value)
        }
    }

    fun toggleShowScaleNum() = update { it.copy(showScaleNum = !it.showScaleNum) }
}