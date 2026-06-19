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
    object Feed : AppScreens

    @Serializable
    object IGive : AppScreens

    @Serializable
    object TheyGive : AppScreens

    @Serializable
    object Profile : AppScreens

    @Serializable
    data class EditThing(val itemId: String) : AppScreens

    @Serializable
    data class ApplicationForm(val thingId: String) : AppScreens


    @Serializable
    data class ThingDetail(val itemId: String) : AppScreens

}


