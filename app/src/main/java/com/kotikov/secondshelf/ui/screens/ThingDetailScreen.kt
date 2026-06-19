package com.kotikov.secondshelf.ui.screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingDetailScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onWantToTakeClick: (String) -> Unit
) {
    // Mock данные вещи
    val mockThing = remember {
        mapOf(
            "title" to "Велосипед горный б/у",
            "imageUrls" to listOf("https://unsplash.com", null, null, null, null),
            "city" to "Москва",
            "category" to "Спорт",
            "description" to "Продаю свой горный велосипед в отличном состоянии. Использовался только по выходным для прогулок в парке. Все механизмы работают исправно, тормоза новые. Отдаю, потому что переехал в другой город и не могу забрать с собой."
        )
    }

    Scaffold(
        topBar = { TopGradientBar(text = "Вещь", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1) Название вещи
            Text(
                text = mockThing["title"] as String,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 2) Фотографии (кликабельные)
            DetailPhotosSection(
                imageUrls = mockThing["imageUrls"] as List<String?>,
                onPhotoClick = { index -> println("Открыть фото #$index") }
            )

            // Город
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = mockThing["city"] as String,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Категория
            Text(
                text = mockThing["category"] as String,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            // Описание
            Text(
                text = mockThing["description"] as String,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Кнопка "Хочу забрать"
            Button(
                onClick = { onWantToTakeClick(itemId) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Хочу забрать", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun DetailPhotosSection(
    imageUrls: List<String?>,
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                    SubcomposeAsyncImage(
                        model = url,
                        contentDescription = "Фото $index",
                        modifier = Modifier.fillMaxSize(),
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
                } else {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Нет фото",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}