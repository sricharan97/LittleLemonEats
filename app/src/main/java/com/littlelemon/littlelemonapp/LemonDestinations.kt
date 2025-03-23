package com.littlelemon.littlelemonapp

import androidx.navigation.navArgument

/**
 * Contract for information needed on every LittleLemon navigation destination
 */
interface LemonDestination {
    val route: String

}

// LittleLemon app navigation destinations

object Onboarding : LemonDestination {
    override val route = "onboarding"
}

object Home : LemonDestination {
    override val route = "home"
}

object Profile : LemonDestination {
    override val route = "profile"
}

object MenuItem : LemonDestination {
    override val route = "menu_item"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
    val arguments = listOf(
        navArgument(itemIdArg) { type = androidx.navigation.NavType.StringType }
    )
}

object Checkout : LemonDestination {
    override val route = "checkout"
}