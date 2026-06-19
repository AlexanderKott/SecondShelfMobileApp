package com.kotikov.secondshelf.ui.screens.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kotikov.secondshelf.ui.screens.ApplicationFormScreen
import com.kotikov.secondshelf.ui.screens.EditItemScreen
import com.kotikov.secondshelf.ui.screens.FeedScreen
import com.kotikov.secondshelf.ui.screens.IGiveScreen
import com.kotikov.secondshelf.ui.screens.ItemCandidatesScreen
import com.kotikov.secondshelf.ui.screens.ProfileScreen
import com.kotikov.secondshelf.ui.screens.SplashScreen
import com.kotikov.secondshelf.ui.screens.TheyGiveScreen
import com.kotikov.secondshelf.ui.screens.ThingDetailScreen

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    bottomBar: SharedBottomBar,
    feedListState: LazyListState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash,
        modifier = modifier,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 50,
                    easing = LinearEasing
                )
            )
        },

        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 50,
                    easing = LinearEasing
                )
            )
        },
    ) {
        composable<AppScreens.Splash> {
            SplashScreen(onLoadingComplete = {
                navController.navigate(AppScreens.Feed) {
                    popUpTo(AppScreens.Splash) { inclusive = true }
                }
            })
        }

        composable<AppScreens.Feed> {
            FeedScreen(
                bottomBar = bottomBar,
                feedListState = feedListState,
                onThingClick = { id -> navController.navigate(AppScreens.ThingDetail(id)) }
            )
        }


        composable<AppScreens.IGive> {
            IGiveScreen(
                bottomBar = bottomBar,
                onEditClick = { id -> navController.navigate(AppScreens.EditThing(id)) },
                onCandidatesClick = { id -> navController.navigate(AppScreens.ItemCandidates(id)) },
            )
        }

        composable<AppScreens.TheyGive> {
            TheyGiveScreen(
                bottomBar = bottomBar,
                onThingClick = { id -> navController.navigate(AppScreens.ThingDetail(id)) }
            )
        }

        composable<AppScreens.Profile> { ProfileScreen(bottomBar) }

        composable<AppScreens.ItemCandidates> { navBackStackEntry ->
            val route: AppScreens.ItemCandidates = navBackStackEntry.toRoute()
            ItemCandidatesScreen(
                itemId = route.itemId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AppScreens.EditThing> { navBackStackEntry ->
            val route: AppScreens.EditThing = navBackStackEntry.toRoute()
            EditItemScreen(
                itemId = route.itemId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AppScreens.ThingDetail> { navBackStackEntry ->
            val route: AppScreens.ThingDetail = navBackStackEntry.toRoute()
            ThingDetailScreen(
                itemId = route.itemId,
                onBackClick = { navController.popBackStack() },
                onWantToTakeClick = { id -> navController.navigate(AppScreens.ApplicationForm(id)) }
            )
        }

        composable<AppScreens.ApplicationForm> { navBackStackEntry ->
            val route: AppScreens.ApplicationForm = navBackStackEntry.toRoute()
            ApplicationFormScreen(
                thingId = route.thingId,
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { application ->
                    println("Заявка отправлена: ${application.thingId}, текст: ${application.applicationText}")
                    navController.popBackStack<AppScreens.Feed>(inclusive = false)
                }
            )
        }

    }
}
