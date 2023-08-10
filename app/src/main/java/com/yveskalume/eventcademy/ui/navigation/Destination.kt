package com.yveskalume.eventcademy.ui.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

sealed class Destination(val route: String) {
    object AuthScreen : Destination("auth")
    object HomeScreen : Destination("home")
    object EventDetailScreen : Destination("event-detail/{eventUid}") {
        fun createRoute(eventUid: String): String {
            return "event-detail/$eventUid"
        }
    }
    object BookmarkScreen : Destination("bookmark")
    object SettingsScreen : Destination("settings")
}
fun NavDestination?.isCurrent(destination: Destination): Boolean {
    return this?.hierarchy?.any { it.route == destination.route } == true
}