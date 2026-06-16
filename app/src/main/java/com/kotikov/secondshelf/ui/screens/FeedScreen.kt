package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.kotikov.secondshelf.ui.screens.components.SearchField
import com.kotikov.secondshelf.ui.screens.components.SelectionDialog
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar
import com.kotikov.secondshelf.ui.screens.components.ThingCard
import com.kotikov.secondshelf.ui.screens.models.FeedThing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    bottomBar: SharedBottomBar,
    feedListState : LazyListState
) {

    val onSearchClick : (String) -> Unit = {}

    val coroutineScope = rememberCoroutineScope ()
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { TopGradientBar("✨ Поиск вещей") },
        bottomBar = bottomBar
    ) { padding ->

        val mockFeedThings = remember {
            List(20) { index ->
                FeedThing(
                    id = "id_item_$index",
                    title = if (index % 2 == 0) "Шерстяное пальто №$index" else "Очень длинное название вещи, которое точно превысит лимит в пятьдесят символов",
                    city = "Москва",
                    description = "Описание для вещи под номером $index. Тут содержится подробный текст о том, в каком состоянии находится вещь, кому она может подойти.",
                    imageUrl = if (index % 3 == 0) "https://unsplash.com" else null,
                    category = null
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                SearchField( onSearchClick)
                Spacer(modifier = Modifier.height(4.dp))
                CityAndCategoryRow()
            }
            Spacer(modifier = Modifier.height(8.dp))

            PullToRefreshBox (
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
            ) {
            ThingsList(
                state = feedListState,
                feedThings = mockFeedThings,
                onThingClick = {}
            )
        }
        }
    }
}

@Composable
private fun ThingsList(
    state: LazyListState,
    feedThings: List<FeedThing>,
    onThingClick: (String) -> Unit
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 4.dp,
                end = 4.dp
            ),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = feedThings,
            key = { thing -> thing.id }
        ) { thing ->
            ThingCard(
                feedThing = thing,
                onCardClick = onThingClick
            )
        }
    }
}

@Composable
private fun CityAndCategoryRow(){
    var showCityDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    var selectedCity by remember { mutableStateOf("Москва") }
    var selectedCategory by remember { mutableStateOf("Все категории") }
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


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { showCityDialog = true },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(text = "Город: $selectedCity")
        }

        OutlinedButton(
            onClick = { showCategoryDialog = true },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(text = selectedCategory)
        }
    }
}

