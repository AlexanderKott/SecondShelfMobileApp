package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.ApplicationItem
import com.kotikov.secondshelf.ui.screens.models.ApplicationStatus
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheyGiveScreen(
    bottomBar: SharedBottomBar,
    onThingClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val mockApplications = remember {
        List(8) { index ->
            val status = when (index % 3) {
                0 -> ApplicationStatus.PENDING
                1 -> ApplicationStatus.APPROVED
                else -> ApplicationStatus.REJECTED
            }
            ApplicationItem(
                id = "application_$index",
                thingId = "thing_$index",
                thingTitle = if (index % 2 == 0) "Велосипед горный б/у" else "Куртка зимняя очень теплая, размер L",
                thingImageUrl = if (index % 2 == 0) "https://unsplash.com" else null,
                status = status,
                ownerContacts = if (status == ApplicationStatus.APPROVED) {
                    "Telegram: @owner_$index\nТелефон: +7 (999) ${100 + index}-00-00"
                } else null
            )
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopGradientBar("Мне дарят") },
        bottomBar = bottomBar
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    isRefreshing = true
                    delay(1500)
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = mockApplications,
                    key = { it.id }
                ) { application ->
                    ApplicationCard(
                        application = application,
                        onThingClick = { onThingClick(application.thingId) },
                        onGiveUpClick = { id ->
                            println("Отказ от заявки: $id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationCard(
    application: ApplicationItem,
    onThingClick: () -> Unit,
    onGiveUpClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isApproved = application.status == ApplicationStatus.APPROVED

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = if (isApproved) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.error)
        } else {
            null
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Верхняя часть: фото + название + статус (кликабельная)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThingClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                ApplicationThingImage(imageUrl = application.thingImageUrl)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.thingTitle,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatusBadge(status = application.status)
                }
            }

            // Поле контактов (только для одобренных)
            if (isApproved && application.ownerContacts != null) {
                Spacer(modifier = Modifier.height(12.dp))
                ContactsField(contacts = application.ownerContacts)
            }

            // Кнопка "Отказаться" — всегда видна
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onGiveUpClick(application.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Отказаться от заявки")
            }
        }
    }
}

@Composable
private fun ApplicationThingImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = "Фото вещи",
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentScale = ContentScale.Crop,
        loading = {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        },
        error = {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                )
            }
        }
    )
}

@Composable
private fun StatusBadge(
    status: ApplicationStatus,
    modifier: Modifier = Modifier
) {
    val (text, containerColor, contentColor) = when (status) {
        ApplicationStatus.PENDING -> Triple(
            "Ожидает",
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurfaceVariant
        )
        ApplicationStatus.APPROVED -> Triple(
            "Вам одобрено",
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer
        )
        ApplicationStatus.REJECTED -> Triple(
            "Отказано",
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ContactsField(
    contacts: String,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Контакты владельца",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contacts,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(contacts))
                        println("Контакты скопированы в буфер обмена")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Скопировать контакты",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}