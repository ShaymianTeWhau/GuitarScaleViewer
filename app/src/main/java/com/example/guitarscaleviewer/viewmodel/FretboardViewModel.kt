package com.example.guitarscaleviewer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FretboardViewModel : ViewModel() {

    val uiState = MutableStateFlow(
        FretboardUiState()
    )
}