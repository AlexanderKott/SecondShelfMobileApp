package com.kotikov.secondshelf.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun SearchField( onSearchClick: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Что ищите?") },
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearchClick(searchQuery)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Искать"
                )
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.small
    )
}