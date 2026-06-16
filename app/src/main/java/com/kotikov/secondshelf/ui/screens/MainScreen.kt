package com.kotikov.secondshelf.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kotikov.secondshelf.ui.screens.navigation.AppNavigationGraph
import com.kotikov.secondshelf.ui.screens.navigation.BottomBar.BottomNavigationTab
import com.kotikov.secondshelf.ui.screens.navigation.SharedBottomBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val feedListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val sharedBottomBar: SharedBottomBar = {

        Column {
            HorizontalDivider(
                thickness = 1.dp, // Толщина линии
                color = Color.Black.copy(alpha = 0.15f) // Черный цвет с легкой прозрачностью (чтобы не смотрелся слишком грубо)
            )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            BottomNavigationTab.items.forEach { tab ->
                val isSelected = currentDestination?.hasRoute(tab.route::class) == true

                NavigationBarItem(
                    selected = isSelected,
                    label = {
                        Text(
                            text = stringResource(id = tab.labelRes),
                            style = if (isSelected) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall
                        )
                    },
                    icon = {
                        if (tab.badgeCount != null && tab.badgeCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        modifier = Modifier.offset(
                                            x = 6.dp,
                                            y = (-4).dp
                                        ),
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = tab.badgeCount.toString())
                                    }
                                }
                            ) {
                                tab.icon()
                            }
                        } else {
                            tab.icon()
                        }
                    },
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFD32F2F),
                        selectedTextColor = Color(0xFFD32F2F),
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Black
                    )
                )
            }
        }
    }
    }

    AppNavigationGraph(
        navController = navController,
        bottomBar = sharedBottomBar,
        feedListState = feedListState
    )
}
