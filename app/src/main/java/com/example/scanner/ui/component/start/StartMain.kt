package com.example.scanner.ui.component.start

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StartMain(
    projectId: Int?
) {
    println(projectId)
    Row {
        Text("projectId")
        Text(projectId.toString())
    }
}