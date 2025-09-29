package com.example.guitarscaleviewer.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guitarscaleviewer.R
import com.example.guitarscaleviewer.model.MINOR_SCALE_EXAMPLE
import com.example.guitarscaleviewer.model.Scale
import com.example.guitarscaleviewer.model.allKeys
import com.example.guitarscaleviewer.model.getScales
import com.example.guitarscaleviewer.model.loadScalesFromAssets
import com.example.guitarscaleviewer.ui.components.Fretboard
import com.example.guitarscaleviewer.viewmodel.FretboardUiState
import com.example.guitarscaleviewer.viewmodel.FretboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    uiState: FretboardUiState,
    onRandom: () -> Unit,
    onShowScalePicker: () -> Unit,
    onShowKeyPicker: () -> Unit,
    onToggleShowScaleNum: () -> Unit
){
    TopAppBar(
        title = { Text("GSViewer") },
        actions = {
            // random button
            OutlinedButton(
                onClick = onRandom
            ) {
                Text("Randomize")
            }
            // scale picker button
            OutlinedButton (
                onClick = onShowScalePicker,
            ) {
                Text(uiState.scale.name)
            }
            // key picker button
            OutlinedButton(onClick =  onShowKeyPicker) {
                Text(uiState.tonicNote)
            }
            // scale num toggle button
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

@Composable
fun ScalePicker(
    visible: Boolean = false,
    onDismiss: () -> Unit,
    onScalePress: (Scale) -> Unit
){
    if (!visible) return
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ){
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 520.dp)
            ){
                val allScales = loadScalesFromAssets(LocalContext.current)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(allScales){ scale ->
                        OutlinedButton(
                            onClick = { onScalePress(scale); onDismiss() }
                        ) {
                            Text(scale.name)
                        }
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun KeyPicker(
    visible: Boolean = false,
    onDismiss: () -> Unit,
    onKeyPress: (String) -> Unit
){
    if (!visible) return

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ){
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 520.dp)
            ){
                val keys = allKeys
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                ) {
                    items(keys){ key ->
                        TextButton(
                            onClick = { onKeyPress(key); onDismiss()}
                        ){
                            Text(
                                text = key,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

// viewModel aware composable
@Composable
fun FretboardScreen(viewModel: FretboardViewModel){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scales = remember { loadScalesFromAssets(context) }

    LaunchedEffect(Unit){
        viewModel.setScales(scales)
    }

    FretboardScreen(
        uiState = uiState,
        onRandom = { viewModel.randomizeScaleAndKey() },
        onScalePress = viewModel::updateScale,
        onKeyPress = viewModel::updateKey,
        onToggleShowScaleNum = { viewModel.toggleShowScaleNum() },
    )
}

// viewModel unaware composable
@Composable
fun FretboardScreen(
    uiState: FretboardUiState,
    onRandom: () -> Unit = {},
    onScalePress: (Scale) -> Unit = {},
    onKeyPress: (String) -> Unit = {},
    onToggleShowScaleNum: () -> Unit = {}
) {
    var showKeyPicker by rememberSaveable { mutableStateOf(false) }
    var showScalePicker by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                uiState = uiState,
                onRandom = onRandom,
                onShowScalePicker = {showScalePicker = true},
                onShowKeyPicker = {showKeyPicker = true},
                onToggleShowScaleNum = onToggleShowScaleNum
            )
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
    ScalePicker(
        visible = showScalePicker,
        onDismiss = { showScalePicker = false },
        onScalePress = onScalePress
    )
    KeyPicker(
        visible = showKeyPicker,
        onDismiss = { showKeyPicker = false },
        onKeyPress = onKeyPress
    )
}

// Previews
val previewUiState: FretboardUiState = FretboardUiState(
    numStrings = 6,
    numFrets = 12,
    fretNotes = MINOR_SCALE_EXAMPLE
)

@Preview(
    name = "Fretboard Horizontal",
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun FretboardScreenPreviewHorizontal() {
    FretboardScreen(uiState = previewUiState)
}

@Preview(
    name = "Fretboard Vertical",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun FretboardScreenPreviewVertical() {
    FretboardScreen(uiState = previewUiState)
}

