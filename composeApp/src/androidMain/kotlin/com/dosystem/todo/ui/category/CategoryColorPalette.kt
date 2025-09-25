package com.dosystem.todo.ui.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.dosystem.todo.getColor

@Composable
fun CategoryColorPalette(
    modifier: Modifier = Modifier,
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    var color by remember { mutableStateOf(selectedColor) }
    val colors = listOf(
        "red", "yellow", "green", "blue", "gray"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        for (idx in colors.indices) {
            Button(
                onClick = {
                    color = colors[idx]
                    onColorSelected(color)
                },
                elevation = null,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = getColor(colors[idx]),
                    disabledContainerColor = getColor(colors[idx])
                ),
                shape = RectangleShape,
                border = BorderStroke(2.dp, if (color == colors[idx]) Color.Black else Color.Transparent),
                modifier = modifier.size(width = 50.dp, height = 25.dp)
            ) {}
            Spacer(modifier.width(8.dp))
        }
    }
}