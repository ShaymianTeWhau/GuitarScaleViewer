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
import com.example.guitarscaleviewer.R
import com.example.guitarscaleviewer.ui.components.Fretboard
import com.example.guitarscaleviewer.ui.components.exampleFretNotes
import com.example.guitarscaleviewer.viewmodel.FretboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(){
    var showScaleNumbers by remember{ mutableStateOf(false) }
    TopAppBar(
        title = { Text("GSViewer") },
        actions = {
            IconButton(onClick = { showScaleNumbers = !showScaleNumbers }) {
                Icon(
                    painter = if(showScaleNumbers) {
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
    FretboardScreen()
}

// viewModel unaware composable
@Composable
fun FretboardScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar()
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ){ innerPadding ->
        Fretboard(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .aspectRatio(16f / 5f),
            numStrings = 6,
            fretNotes = exampleFretNotes,
            showScaleNum = true
        )
    }
}

@Preview(
    name = "Fretboard Horizontal",
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun FretboardScreenPreviewHorizontal() {
    FretboardScreen()
}

@Preview(
    name = "Fretboard Vertical",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun FretboardScreenPreviewVertical() {
    FretboardScreen()
}

