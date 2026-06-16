package com.kotikov.secondshelf.ui.screens.navigation

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable


typealias SharedBottomBar = @Composable () -> Unit


sealed interface AppScreens {

    @Serializable
    object Splash : AppScreens

    @Serializable
    data class ItemCandidates(val itemId: String) : AppScreens

    @Serializable
    object Feed : AppScreens          // Лента

    @Serializable
    object IGive : AppScreens         // Я дарю

    @Serializable
    object TheyGive : AppScreens      // Мне дарят

    @Serializable
    object Profile : AppScreens       // Личный кабинет

    @Serializable
    data class EditThing(val itemId: String) : AppScreens
}


