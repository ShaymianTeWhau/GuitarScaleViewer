package com.example.guitarscaleviewer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
    onToggleShowScaleNum: () -> Unit,
    onShowSettings: () -> Unit
){
    TopAppBar(
        title = { Text("GSViewer: ${uiState.instrument.displayName}") },
        actions = {
            // random button
            OutlinedButton(
                onClick = onRandom
            ) {
                Text("Randomize")
            }
            // scale picker button
            OutlinedButton (
                modifier = Modifier.width(200.dp),
                onClick = onShowScalePicker,
            ) {
                Text(uiState.scale.name)
            }
            // key picker button
            OutlinedButton(
                modifier = Modifier.width(85.dp),
                onClick = onShowKeyPicker
            ) {
                Text(uiState.tonicNote)
            }
            // scale num toggle button
            IconButton(onClick = onToggleShowScaleNum) {
                val iconSize = if(uiState.showScaleNum) 40.dp else 30.dp
                Icon(
                    painter = if(uiState.showScaleNum) {
                        painterResource(id = R.drawable.roman1)
                    } else painterResource(id = R.drawable.quarter_note),
                    contentDescription = "Toggle notes/numbers",
                    modifier = Modifier.size(iconSize)
                )
            }
            // settings button
            IconButton(onClick = onShowSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.LightGray
        )
    )
}

// scale picker dialog box
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
                val allScales = loadScalesFromAssets(LocalContext.current) // gets all scale options
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

// key picker dialog box
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
                val keys = allKeys // gets all key options
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

// settings menu dialog box
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    uiState: FretboardUiState,
    visible: Boolean = false,
    onDismiss: () -> Unit,
    onInstrumentPress: (String) -> Unit,
    onStringCountChange: (Int) -> Unit,
    onFretCountChange: (Int) -> Unit
){
    if (!visible) return

    // options for each dropdown menu
    val instrumentOptions = listOf("Guitar", "Bass")
    val bassStringOptions = listOf("4", "5", "6")
    val guitarStringOptions = listOf("6", "7", "8")
    var stringOptions = if(uiState.instrument.displayName == "Guitar") guitarStringOptions else bassStringOptions
    val fretOptions = listOf("15", "22", "24")

    // dropdown menu expanded state
    var instrumentsExpanded by remember { mutableStateOf(false) }
    var stringsExpanded by remember{ mutableStateOf(false) }
    var fretsExpanded by remember{ mutableStateOf(false) }

    // dropdown menu current selection
    var selectedInstrument by remember { mutableStateOf(uiState.instrument.displayName) }
    var selectedStrings by remember { mutableStateOf(uiState.numStrings.toString()) }
    var selectedFrets by remember { mutableStateOf(uiState.numFrets.toString()) }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ){
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Text("Instrument Options", style = MaterialTheme.typography.titleMedium)

                // instrument dropdown
                Text("Instrument: ", style = MaterialTheme.typography.titleSmall)
                ExposedDropdownMenuBox(
                    expanded = instrumentsExpanded,
                    onExpandedChange = { instrumentsExpanded = !instrumentsExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedInstrument,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = instrumentsExpanded,
                        onDismissRequest = { instrumentsExpanded = false }
                    ) {
                        instrumentOptions.forEach{ option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedInstrument = option
                                    onInstrumentPress(option)
                                    if(option == "Guitar"){
                                        stringOptions = guitarStringOptions
                                        selectedStrings = "6"
                                    } else{
                                        stringOptions = bassStringOptions
                                        selectedStrings = "4"
                                    }
                                    instrumentsExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ){
                        // strings dropdown
                        Text("Strings: ", style = MaterialTheme.typography.titleSmall)
                        ExposedDropdownMenuBox(
                            expanded = stringsExpanded,
                            onExpandedChange = { stringsExpanded = !stringsExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedStrings,
                                onValueChange = {},
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = stringsExpanded,
                                onDismissRequest = { stringsExpanded = false }
                            ) {
                                stringOptions.forEach{ option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedStrings = option
                                            onStringCountChange(option.toInt())
                                            stringsExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                            ){

                        // frets dropdown
                        Text("Frets:", style = MaterialTheme.typography.titleSmall)
                        ExposedDropdownMenuBox(
                            expanded = fretsExpanded,
                            onExpandedChange = { fretsExpanded = !fretsExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedFrets,
                                onValueChange = {},
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = fretsExpanded,
                                onDismissRequest = { fretsExpanded = false }
                            ) {
                                fretOptions.forEach{ option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedFrets = option
                                            onFretCountChange(option.toInt())
                                            fretsExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                }


                TextButton(onClick = onDismiss) {
                    Text("Exit")
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
        onInstrumentPress = viewModel::updateInstrument,
        onStringCountChange = viewModel::updateStringCount,
        onFretCountChange = viewModel::updateFretCount
    )
}

// viewModel unaware composable
@Composable
fun FretboardScreen(
    uiState: FretboardUiState,
    onRandom: () -> Unit = {},
    onScalePress: (Scale) -> Unit = {},
    onKeyPress: (String) -> Unit = {},
    onToggleShowScaleNum: () -> Unit = {},
    onInstrumentPress: (String) -> Unit = {},
    onStringCountChange: (Int) -> Unit = {},
    onFretCountChange: (Int) -> Unit = {}
) {
    // visibility state of dialog box's
    var showKeyPicker by rememberSaveable { mutableStateOf(false) }
    var showScalePicker by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                uiState = uiState,
                onRandom = onRandom,
                onShowScalePicker = {showScalePicker = true},
                onShowKeyPicker = {showKeyPicker = true},
                onToggleShowScaleNum = onToggleShowScaleNum,
                onShowSettings = {showSettings = true}
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

    // dialog box's
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
    SettingsMenu(
        uiState = uiState,
        visible = showSettings,
        onDismiss = { showSettings = false },
        onInstrumentPress = onInstrumentPress,
        onStringCountChange = onStringCountChange,
        onFretCountChange = onFretCountChange
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

