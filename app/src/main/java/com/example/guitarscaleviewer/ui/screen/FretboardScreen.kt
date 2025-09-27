package com.example.guitarscaleviewer.ui.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guitarscaleviewer.R
import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.IntervalModifier
import com.example.guitarscaleviewer.model.createScale
import com.example.guitarscaleviewer.ui.components.Fretboard
import com.example.guitarscaleviewer.viewmodel.FretboardUiState
import com.example.guitarscaleviewer.viewmodel.FretboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(uiState: FretboardUiState, onToggleShowScaleNum: () -> Unit){
    TopAppBar(
        title = { Text("GSViewer") },
        actions = {
            IconButton(onClick = onToggleShowScaleNum) {
                Icon(
                    painter = if(uiState.showScaleNum) {
                        painterResource(id = R.drawable.roman1)
                    } else painterResource(id = R.drawable.quarter_note),
                    contentDescription = "Toggle notes/numbers"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.LightGray
        )
    )
}

// viewModel aware composable
@Composable
fun FretboardScreen(viewModel: FretboardViewModel){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FretboardScreen(
        uiState = uiState,
        onToggleShowScaleNum = { viewModel.toggleShowScaleNum() }
    )
}

// viewModel unaware composable
@Composable
fun FretboardScreen(
    uiState: FretboardUiState,
    onToggleShowScaleNum: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(uiState = uiState, onToggleShowScaleNum = onToggleShowScaleNum)
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ){ innerPadding ->
        Fretboard(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .aspectRatio(16f / 5f),
            numStrings = uiState.numStrings,
            numFrets = uiState.numFrets,
            fretNotes = uiState.fretNotes,
            showScaleNum = uiState.showScaleNum
        )
    }
}

// Previews
val previewUiState: FretboardUiState = FretboardUiState(
    numStrings = 6,
    fretNotes = createScale(
        tonicNote = "A",
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
)

@Preview(
    name = "Fretboard Horizontal",
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun FretboardScreenPreviewHorizontal() {
    FretboardScreen(previewUiState)
}

@Preview(
    name = "Fretboard Vertical",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun FretboardScreenPreviewVertical() {
    FretboardScreen(previewUiState)
}

