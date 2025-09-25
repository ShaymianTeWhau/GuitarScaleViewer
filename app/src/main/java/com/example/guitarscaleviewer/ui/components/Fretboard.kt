package com.example.guitarscaleviewer.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Fretboard(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 5f),
    numStrings: Int = 6,
    numFrets: Int = 15,
    standardFretMarkers: Set<Int> = setOf(3, 5, 7, 9, 12, 15, 17, 19, 21, 24),
    specialFretMarkers: Set<Int> = setOf(12, 24),
    minStringWidth: Float = 1f,
    maxStringWidth: Float = 4f
) {

    val fretStroke = with(LocalDensity.current) { 1.dp.toPx() }
    val markerRadius = with(LocalDensity.current) { 5.dp.toPx() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3E7D3))
    ){

        val canvasWidth = size.width
        val canvasHeight = size.height

        val nutToFretWidthRatio = 0.3f;
        val fretWidth = canvasWidth / (nutToFretWidthRatio + numFrets)

        // draw nut
        drawRect(
            color = Color(0xFF6D5C4D),
            topLeft = Offset(0f, 0f),
            size = Size(
                width = fretWidth * nutToFretWidthRatio,
                height = canvasHeight
            )
        )

        val fretboardStartX = nutToFretWidthRatio * fretWidth
        // draw fret bars
        for (f in 0 until numFrets) {
            var x = f * fretWidth + fretboardStartX
            drawLine(
                color = Color(0xFF8C8C8C),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = fretStroke
            )

        }

        // draw fret markers
        val centerY = canvasHeight / 2
        val quarterY = canvasHeight / 4
        val threeQuarterY = quarterY * 3
        for (m in standardFretMarkers){
            val x = (m-1) * fretWidth + fretboardStartX + (fretWidth / 2)

            if(specialFretMarkers.contains(m)){
                // draw special fret markers
                drawCircle(
                    color = Color(0xFF6D5C4D),
                    radius = markerRadius,
                    center = Offset(x, quarterY)
                )
                drawCircle(
                    color = Color(0xFF6D5C4D),
                    radius = markerRadius,
                    center = Offset(x, threeQuarterY)
                )

            } else {
                // draw standard fret marker
                drawCircle(
                    color = Color(0xFF6D5C4D),
                    radius = markerRadius,
                    center = Offset(x, centerY)
                )
            }

        }

        // draw strings
        val stringMargin = canvasHeight * 0.08f
        val stringDistance = (canvasHeight - (stringMargin*2)) / (numStrings - 1)
        for (s in 0 until numStrings){
            val y = stringMargin + s * stringDistance

            // simulate string size differences
            val fraction = s.toFloat() / (numStrings - 1)
            val stroke = minStringWidth + (maxStringWidth - minStringWidth) * fraction

            drawLine(
                color = Color(0xFF4A4A4A),
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = stroke
            )
        }

        // draw notes

    }
}

@Preview(showBackground = true)
@Composable
fun FretboardPreview6(){
    Fretboard(numStrings = 6)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreview7(){
    Fretboard(numStrings = 7)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreview8(){
    Fretboard(numStrings = 8)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass4(){
    Fretboard(numStrings = 4, minStringWidth = 4f, maxStringWidth = 8f)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass5(){
    Fretboard(numStrings = 5, minStringWidth = 4f, maxStringWidth = 8f)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass6(){
    Fretboard(numStrings = 6, minStringWidth = 4f, maxStringWidth = 8f)
}
