package com.kotikov.secondshelf.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun PhotoViewer(
    imageUrls: List<Any?>,  // Теперь принимает и Int (R.drawable.xxx), и String (URL)
    initialIndex: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val pagerState = rememberPagerState(
            initialPage = initialIndex.coerceIn(0, (imageUrls.size - 1).coerceAtLeast(0))
        ) { imageUrls.size }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(
                    imageModel = imageUrls[page],
                    onDismiss = onDismiss
                )
            }

            // Кнопка закрытия
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .statusBarsPadding()
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = Color.White
                )
            }

            // Счетчик фото
            if (imageUrls.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp)
                        .statusBarsPadding()
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${imageUrls.size}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoomableImage(
    imageModel: Any?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Состояния для pinch-to-zoom
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Состояния для свайпа вниз
    var dragOffsetY by remember { mutableStateOf(0f) }

    // Вычисляем прозрачность фона на основе свайпа
    val backgroundAlpha = (1f - dragOffsetY / 500f).coerceIn(0f, 1f)
    val dragScale = (1f - dragOffsetY / 1000f).coerceIn(0.5f, 1f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = backgroundAlpha))
    ) {
        SubcomposeAsyncImage(
            model = imageModel,
            contentDescription = "Фото",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = if (dragOffsetY > 0f) dragScale else scale,
                    scaleY = if (dragOffsetY > 0f) dragScale else scale,
                    translationX = if (dragOffsetY > 0f) 0f else offsetX,
                    translationY = if (dragOffsetY > 0f) dragOffsetY else offsetY
                )
                .pointerInput(Unit) {
                    if (scale <= 1.01f) {
                        // Режим 1: свайп вниз для закрытия (фото не зумировано)
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (dragAmount.y > 0) {
                                    dragOffsetY = (dragOffsetY + dragAmount.y).coerceAtLeast(0f)
                                }
                            },
                            onDragEnd = {
                                if (dragOffsetY > 200f) {
                                    onDismiss()
                                } else {
                                    dragOffsetY = 0f
                                }
                            },
                            onDragCancel = {
                                dragOffsetY = 0f
                            }
                        )
                    } else {
                        // Режим 2: pinch-to-zoom и pan (фото зумировано)
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            val maxOffsetX = (size.width * (scale - 1)) / 2
                            val maxOffsetY = (size.height * (scale - 1)) / 2
                            offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)
                            offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)

                            if (scale <= 1.01f) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
                },
            contentScale = ContentScale.Fit
        )
    }
}