package com.kotikov.secondshelf.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotikov.secondshelf.ui.screens.components.SelectionDialog
import com.kotikov.secondshelf.ui.screens.components.ThingCategorySelector
import com.kotikov.secondshelf.ui.screens.components.ThingCitySelector
import com.kotikov.secondshelf.ui.screens.components.ThingDescriptionField
import com.kotikov.secondshelf.ui.screens.components.ThingPhotosSection
import com.kotikov.secondshelf.ui.screens.components.ThingTitleField
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.EditThingFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddThingScreen(
    onBackClick: () -> Unit,
    onThingCreated: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var showCityDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() &&
            selectedCity.isNotBlank() &&
            selectedCategory.isNotBlank() &&
            description.isNotBlank()

    Scaffold(
        topBar = { TopGradientBar(text = "Добавить вещь", onBackClick = onBackClick) },
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
            ThingTitleField(
                value = title,
                onValueChange = { title = it },
                initiallyEditable = true
            )

            ThingPhotosSection(
                imageUrls = emptyList(),
                onPhotoClick = { index -> println("Photo click index: $index") }
            )

            ThingCitySelector(
                cityName = if (selectedCity.isBlank()) "Не выбран" else selectedCity,
                onClick = { showCityDialog = true }
            )

            ThingCategorySelector(
                categoryName = if (selectedCategory.isBlank()) "Не выбрана" else selectedCategory,
                onClick = { showCategoryDialog = true }
            )

            ThingDescriptionField(
                value = description,
                onValueChange = { description = it },
                initiallyEditable = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val newThing = EditThingFormState(
                        id = "", // ID сгенерирует бэкенд
                        title = title,
                        imageUrls = emptyList(),
                        city = selectedCity,
                        category = selectedCategory,
                        description = description
                    )
                    println("Создание новой вещи: $newThing")
                    onThingCreated()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                enabled = isFormValid
            ) {
                Text("Создать вещь", style = MaterialTheme.typography.bodyLarge)
            }
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
            onDismiss = { showCityDialog = false }
        )
    }

    if (showCategoryDialog) {
        SelectionDialog(
            title = "Выберите категорию",
            options = listOf("Одежда", "Обувь", "Электроника", "Книги", "Другое"),
            onOptionSelected = {
                selectedCategory = it
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }
}