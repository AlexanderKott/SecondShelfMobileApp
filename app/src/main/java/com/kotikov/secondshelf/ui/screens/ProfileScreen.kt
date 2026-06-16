package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotikov.secondshelf.ui.screens.components.SelectionDialog
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    bottomBar: SharedBottomBar,
    modifier: Modifier = Modifier
) {
    var nameAndSurname by remember { mutableStateOf("Иван Иванов") }
    var selectedCity by remember { mutableStateOf("Москва") }
    var contactsInfo by remember { mutableStateOf("Telegram: @ivan_dev\nТелефон: +7 (999) 123-45-67") }

    var showCityDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { TopGradientBar("Личный кабинет") },
        bottomBar = bottomBar
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
            ProfileNameInput(
                value = nameAndSurname,
                onValueChange = { nameAndSurname = it }
            )

            ProfileCitySelector(
                cityName = selectedCity,
                onClick = { showCityDialog = true }
            )

            ProfileContactsInput(
                value = contactsInfo,
                onValueChange = { contactsInfo = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Удалить свой профиль", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }

    if (showCityDialog) {
        SelectionDialog (
            title = "Выберите город",
            options = listOf("Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург"),
            onOptionSelected = {
                selectedCity = it
                showCityDialog = false
            },
            onDismiss = { showCityDialog = false }
        )
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            onConfirm = {
                showDeleteDialog = false
                println("Профиль пользователя успешно удален.")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun ProfileNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditable by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Имя и Фамилия") },
        singleLine = true,
        readOnly = !isEditable,
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isEditable) {
                        // ЗДЕСЬ СРАБАТЫВАЕТ СОХРАНЕНИЕ
                        // Данные из переменной 'value' можно отправить на сервер или в БД
                        println("Имя сохранено: $value")
                    }
                    isEditable = !isEditable
                }
            ) {
                Icon(
                    imageVector = if (isEditable) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditable) "Сохранить" else "Редактировать",
                    tint = if (isEditable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}


@Composable
private fun ProfileCitySelector(
    cityName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Ваш город",
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
                    contentDescription = "Выбрать город",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



@Composable
private fun ProfileContactsInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditable by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Контактные данные") },
        minLines = 3,
        maxLines = 3,
        readOnly = !isEditable,
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isEditable) {
                        // ЗДЕСЬ СРАБАТЫВАЕТ СОХРАНЕНИЕ КОНТАКТОВ
                        println("Контакты сохранены: $value")
                    }
                    isEditable = !isEditable
                }
            ) {
                Icon(
                    imageVector = if (isEditable) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditable) "Сохранить" else "Редактировать",
                    tint = if (isEditable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}


@Composable
private fun DeleteAccountDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удаление профиля") },
        text = { Text("Вы уверены, что хотите безвозвратно удалить свой профиль? Все ваши вещи и история будут стерты.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

