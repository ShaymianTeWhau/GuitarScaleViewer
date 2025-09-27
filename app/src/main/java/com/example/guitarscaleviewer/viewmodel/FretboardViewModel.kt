package com.example.guitarscaleviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FretboardViewModel : ViewModel() {

    val uiState = MutableStateFlow(
        FretboardUiState()
    )

    // update state by creating a new state
    private inline fun update(crossinline block: (FretboardUiState) -> FretboardUiState) {
        viewModelScope.launch {
            uiState.value = block(uiState.value)
        }
    }

    fun toggleShowScaleNum() = update { it.copy(showScaleNum = !it.showScaleNum) }
}