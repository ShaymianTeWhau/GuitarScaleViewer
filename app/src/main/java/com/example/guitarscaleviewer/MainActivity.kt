package com.example.guitarscaleviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.guitarscaleviewer.ui.screen.FretboardScreen
import com.example.guitarscaleviewer.ui.theme.GuitarScaleViewerTheme
import com.example.guitarscaleviewer.viewmodel.FretboardViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FretboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuitarScaleViewerTheme {
                FretboardScreen(viewModel)
            }
        }
    }
}
