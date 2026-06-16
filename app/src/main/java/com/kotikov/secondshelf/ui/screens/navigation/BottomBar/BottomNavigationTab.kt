package com.kotikov.secondshelf.ui.screens.navigation.BottomBar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kotikov.secondshelf.R
import com.kotikov.secondshelf.ui.screens.navigation.AppScreens

sealed class BottomNavigationTab(
    val route: AppScreens,
    val icon: @Composable () -> Unit,
    val badgeCount: Int? = null,
    @StringRes val labelRes: Int
) {
    object IGive : BottomNavigationTab(
        AppScreens.IGive,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.gift_from_me),
                contentDescription = null
            )
        }, badgeCount = 3,
        labelRes = R.string.tab_i_give

    )

    object TheyGive : BottomNavigationTab(
        AppScreens.TheyGive,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.gift_to_me),
                contentDescription = null
            )
        }, labelRes = R.string.tab_they_give,
        badgeCount = 4,
    )

    object Feed : BottomNavigationTab(
        AppScreens.Feed,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.feed),
                contentDescription = null
            )
        }, labelRes = R.string.tab_feed
    )

    object Profile : BottomNavigationTab(
        AppScreens.Profile,
        icon = {
            Icon(
                imageVector = Icons.Default.Person, contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        },
        labelRes = R.string.tab_profile
    )

    companion object {
        val items = listOf(IGive, TheyGive, Feed, Profile)
    }
}