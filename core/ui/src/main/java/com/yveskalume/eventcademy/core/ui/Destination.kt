package com.yveskalume.eventcademy.core.ui

import androidx.navigation.NavController
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
    object CreateEventScreen : Destination("create-event")
    object ProfileScreen : Destination("profile")
}
fun NavDestination?.isCurrent(destination: Destination): Boolean {
    return this?.hierarchy?.any { it.route == destination.route } == true
}

fun NavController.navigate(destination: Destination) {
    navigate(destination.route)
}