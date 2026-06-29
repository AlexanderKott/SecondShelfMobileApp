package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotikov.secondshelf.ui.screens.components.IGiveThingCard
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar
import com.kotikov.secondshelf.ui.screens.models.IGiveThing
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IGiveScreen(
    bottomBar: SharedBottomBar,
    onEditClick: (String) -> Unit,
    onCandidatesClick: (String) -> Unit,
    onThingClick: (String) -> Unit,
    onAddClick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val myGifts = remember {
        List(10) { index ->
            IGiveThing(
                id = "my_gift_$index",
                title = if (index % 2 == 0) "Велосипед горный б/у" else "Куртка зимняя очень теплая, размер L",
                city = "Москва",
                category = "Спорт",
                imageUrl = if (index % 2 == 0) "https://unsplash.com" else null
            )
        }
    }

    Scaffold(
        topBar = { TopGradientBar(text = "Вещи от меня") },
        bottomBar = bottomBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick= onAddClick,
                containerColor = Color(0xFFF44336)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить"
                )
            }
        }
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
                items(items = myGifts, key = { it.id }) { thing ->
                    IGiveThingCard(
                        feedThing = thing,
                        candidatesCount = (thing.id.hashCode() % 5 + 1),
                        onThingClick = onThingClick,
                        onEditClick = onEditClick,
                        onCandidatesClick = onCandidatesClick,
                        onDeleteClick = { id ->
                            println("Триггер удаления вещи с ID: $id")
                        }
                    )
                }
            }
        }
    }
}