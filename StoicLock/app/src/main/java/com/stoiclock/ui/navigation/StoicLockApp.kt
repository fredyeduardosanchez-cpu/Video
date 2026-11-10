package com.stoiclock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stoiclock.ui.home.HomeRoute
import com.stoiclock.ui.library.LibraryRoute
import com.stoiclock.ui.settings.SettingsRoute

enum class TopLevelDestination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Hoy", Icons.Outlined.WbSunny),
    LIBRARY("library", "Biblioteca", Icons.AutoMirrored.Outlined.LibraryBooks),
    SETTINGS("settings", "Bloqueo", Icons.Outlined.Settings),
}

@Composable
fun StoicLockAppRoot(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = { navigateTo(destination) },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.HOME.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(TopLevelDestination.HOME.route) {
                HomeRoute(onOpenSettings = { navigateTo(TopLevelDestination.SETTINGS) })
            }
            composable(TopLevelDestination.LIBRARY.route) { LibraryRoute() }
            composable(TopLevelDestination.SETTINGS.route) { SettingsRoute() }
        }
    }
}
