package com.example.guitarscaleviewer.ui.components


import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guitarscaleviewer.model.FretNote

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
    maxStringWidth: Float = 4f,
    showScaleNum: Boolean = false,
    fretNotes: Set<FretNote>
) {
    val textMeasurer = rememberTextMeasurer()
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
        val fretPositionsX = mutableSetOf<Float>()
        for (f in 0 until (numFrets + 1)) { // add 1 because there is a fret 0 as well
            val x = f * fretWidth + fretboardStartX // save fret positions for later when drawing FretNotes
            fretPositionsX.add(x)
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
        val stringPositionsY = mutableSetOf<Float>() // save string positions for later when drawing FretNotes
        for (s in 0 until numStrings){
            val y = stringMargin + s * stringDistance
            stringPositionsY.add(y)

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
        for(fretNote in fretNotes) {
            if((fretNote.string) > numStrings) continue
            if((fretNote.fret) > numFrets) continue

            var x = fretPositionsX.elementAt(fretNote.fret) - fretWidth / 2
            if(fretNote.fret == 0){
                x = fretboardStartX
            }
            val y = stringPositionsY.elementAt(fretNote.string - 1)

            val circleRadius = fretWidth / 4
            val borderWidth = 2f

            // draw outline
            drawCircle(
                color = fretNote.borderColor,
                radius = circleRadius + borderWidth,
                center = Offset(x, y)
            )
            // draw background
            drawCircle(
                color = fretNote.backgroundColor,
                radius = circleRadius,
                center = Offset(x, y)
            )

            // setup draw label
            val labelText = if(showScaleNum) fretNote.scaleNum else fretNote.note
            val textStyle = TextStyle(
                color = fretNote.textColor,
                fontSize = 10.sp
            )
            val measuredText = textMeasurer.measure(
                text = labelText,
                style = textStyle
            )
            val textWidth = measuredText.size.width
            val textHeight = measuredText.size.height
            val textX = x - (textWidth / 2)
            val textY = y - (textHeight / 2)

            // draw label
            drawText(
                textMeasurer = textMeasurer,
                text = labelText,
                topLeft = Offset(textX, textY),
                style = textStyle
            )
        }
    }
}

val exampleFretNotes = setOf(
    FretNote(fret = 8, string = 6, note = "C", scaleNum = "1"),
    FretNote(fret = 10, string = 5, note = "G", scaleNum = "5"),
    FretNote(fret = 9, string = 4, note = "E", scaleNum = "3"),
    FretNote(fret = 9, string = 3, note = "B", scaleNum = "7"),
    FretNote(fret = 8, string = 2, note = "E", scaleNum = "3"),
    FretNote(fret = 8, string = 1, note = "C", scaleNum = "1")
)

@Preview(showBackground = true)
@Composable
fun FretboardPreview6(){
    Fretboard(numStrings = 6, fretNotes = exampleFretNotes, showScaleNum = true)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreview7(){
    Fretboard(numStrings = 7, fretNotes = exampleFretNotes)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreview8(){
    Fretboard(numStrings = 8, fretNotes = exampleFretNotes)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass4(){
    Fretboard(numStrings = 4, minStringWidth = 4f, maxStringWidth = 8f, fretNotes = exampleFretNotes)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass5(){
    Fretboard(numStrings = 5, minStringWidth = 4f, maxStringWidth = 8f, fretNotes = exampleFretNotes)
}

@Preview(showBackground = true)
@Composable
fun FretboardPreviewBass6(){
    Fretboard(numStrings = 6, minStringWidth = 4f, maxStringWidth = 8f, fretNotes = exampleFretNotes)
}
