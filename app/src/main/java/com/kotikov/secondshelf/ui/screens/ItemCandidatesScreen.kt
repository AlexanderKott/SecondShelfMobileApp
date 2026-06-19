package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.Candidate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ItemCandidatesScreen(
    itemId: String,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val mockCandidates = remember {
        List(10) { index ->
            Candidate(
                id = "candidate_$index",
                firstName = if (index % 2 == 0) "Александр" else "Екатерина",
                lastName = if (index % 3 == 0) "Петров" else "Смирнова",
                photoUrl = if (index % 2 == 0) "https://unsplash.com" else null,
                profileLink = "https://secondshelf.app/user/$index",
                thingTitle = if (index % 2 == 0) "Велосипед горный б/у" else "Куртка зимняя очень теплая, размер L",
                applicationText = "Здравствуйте! Меня зовут ${if (index % 2 == 0) "Александр" else "Екатерина"}, и я очень заинтересован(а) в вашей вещи. " +
                        "Я живу в Москве и готов(а) забрать её в удобное для вас время. " +
                        "Эта вещь очень нужна мне для ${if (index % 2 == 0) "ежедневных поездок на работу" else "теплой зимней прогулки"}. " +
                        "Обещаю бережно относиться к ней и передать дальше, когда она мне больше не понадобится. " +
                        "Спасибо за ваше предложение!"
            )
        }
    }

    Scaffold(
        topBar = { TopGradientBar(text = "Претенденты", onBackClick = onBackClick) }
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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = mockCandidates,
                    key = { it.id }
                ) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onProfileClick = { println("Открыть профиль: ${candidate.profileLink}") },
                        onThingClick = { println("Открыть вещь: ${candidate.thingTitle}") },
                        onRejectClick = { println("Отказать кандидату: ${candidate.id}") },
                        onSelectClick = { println("Выбрать кандидата: ${candidate.id}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun CandidateCard(
    candidate: Candidate,
    onProfileClick: () -> Unit,
    onThingClick: () -> Unit,
    onRejectClick: () -> Unit,
    onSelectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CandidatePhoto(photoUrl = candidate.photoUrl)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${candidate.firstName} ${candidate.lastName}",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    TextButton(
                        onClick = onProfileClick,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Перейти в профиль",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(4.dp))

            // Ссылка на вещь
            Text(
                text = "Выбранная вещь: ${candidate.thingTitle}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThingClick() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Текст заявки
            Text(
                text = candidate.applicationText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Отказать")
                }

                Button(
                    onClick = onSelectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Выбрать")
                }
            }
        }
    }
}

@Composable
private fun CandidatePhoto(
    photoUrl: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = photoUrl,
        contentDescription = "Фото претендента",
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentScale = ContentScale.Crop,
        loading = {
            Box(contentAlignment = Alignment.Center) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        },
        error = {
            Box(contentAlignment = Alignment.Center) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                )
            }
        }
    )
}