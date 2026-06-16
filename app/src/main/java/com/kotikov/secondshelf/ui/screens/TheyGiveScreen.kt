package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar
import com.kotikov.secondshelf.ui.screens.components.TopGradientBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheyGiveScreen(bottomBar: SharedBottomBar) {
    Scaffold(
        topBar = {  TopGradientBar("Мне дарят")},
        bottomBar = bottomBar
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Вещи, которые вам пообещали подарить")
        }
    }
}