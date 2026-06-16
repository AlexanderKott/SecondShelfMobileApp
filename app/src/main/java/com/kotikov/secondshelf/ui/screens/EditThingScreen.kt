package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kotikov.secondshelf.ui.screens.components.SelectionDialog
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.EditThingFormState

@Composable
private fun EditTitleSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditable by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Название вещи") },
        singleLine = true,
        readOnly = !isEditable,
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isEditable) println("Title locked: $value")
                    isEditable = !isEditable
                }
            ) {
                Icon(
                    imageVector = if (isEditable) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditable) "Save" else "Edit",
                    tint = if (isEditable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun EditPhotosSection(
    imageUrls: List<String?>,
    onPhotoClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Фотографии вещи (макс. 5)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) { index ->
                val url = imageUrls.getOrNull(index)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .clickable { onPhotoClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (url != null) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Photo $index",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Add photo",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditCitySection(
    cityName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Город получения",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = cityName, style = MaterialTheme.typography.bodyLarge)
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Select city",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EditCategorySection(
    categoryName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Категория вещи",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = categoryName, style = MaterialTheme.typography.bodyLarge)
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Select category",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EditDescriptionSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditable by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Описание вещи") },
        minLines = 4,
        maxLines = 4,
        readOnly = !isEditable,
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isEditable) println("Description locked")
                    isEditable = !isEditable
                }
            ) {
                Icon(
                    imageVector = if (isEditable) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditable) "Save" else "Edit",
                    tint = if (isEditable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    itemId: String,
    onBackClick: () -> Unit,
) {
    var title by remember { mutableStateOf("initialState.title") }
    var imageUrls by remember { mutableStateOf("initialState.imageUrls") }
    var selectedCity by remember { mutableStateOf("initialState.city") }
    var selectedCategory by remember { mutableStateOf("initialState.category") }
    var description by remember { mutableStateOf("initialState.description") }

    var showCityDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopGradientBar(text = "Редактировать вещь", onBackClick = onBackClick) },

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditTitleSection(value = title, onValueChange = { title = it })

            EditPhotosSection(
                imageUrls = emptyList(),
                onPhotoClick = { index -> println("Photo click index: $index") }
            )

            EditCitySection(cityName = selectedCity, onClick = { showCityDialog = true })

            EditCategorySection(
                categoryName = selectedCategory,
                onClick = { showCategoryDialog = true })
            EditDescriptionSection(value = description, onValueChange = { description = it })
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val updatedData =
                        EditThingFormState(
                            id = "initialState.id",
                            title = title,
                            imageUrls = emptyList(),
                            city = selectedCity,
                            category = selectedCategory,
                            description = description
                        )
                  //  onSaveClick(updatedData)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            { Text("Сохранить изменения", style = MaterialTheme.typography.bodyLarge) }
        }
    }

    if (showCityDialog) {
        SelectionDialog(
            title = "Выберите город",
            options = listOf("Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург"),
            onOptionSelected = {
                selectedCity = it
                showCityDialog = false
            },
            onDismiss = { showCityDialog = false })
    }

    if (showCategoryDialog) {
        SelectionDialog(
            title = "Выберите категория",
            options = listOf("Одежда", "Обувь", "Электроника", "Книги", "Другое"),
            onOptionSelected = {
                selectedCategory = it
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false })
    }
}