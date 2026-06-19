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
import androidx.compose.material3.OutlinedTextField
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
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.ThingApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationFormScreen(
    thingId: String,
    onBackClick: () -> Unit,
    onSubmitClick: (ThingApplication) -> Unit
) {
    var applicationText by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopGradientBar(text = "Заявка на вещь", onBackClick = onBackClick) }
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
            OutlinedTextField(
                value = applicationText,
                onValueChange = { applicationText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Почему отдать вещь нужно мне?") },
                minLines = 6,
                maxLines = 10,
                shape = MaterialTheme.shapes.small
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val application = ThingApplication(
                        thingId = thingId,
                        applicationText = applicationText
                    )
                    onSubmitClick(application)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                enabled = applicationText.isNotBlank()
            ) {
                Text("Отправить заявку", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}